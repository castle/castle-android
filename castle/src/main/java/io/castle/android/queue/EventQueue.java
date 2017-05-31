package io.castle.android.queue;


import android.content.Context;

import com.squareup.tape2.ObjectQueue;
import com.squareup.tape2.QueueFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

import io.castle.android.Castle;
import io.castle.android.CastleLogger;
import io.castle.android.api.CastleAPIService;
import io.castle.android.api.model.Batch;
import io.castle.android.api.model.Event;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Copyright (c) 2017 Castle
 */
public class EventQueue implements Callback<Void> {
    private static final String QUEUE_FILENAME = "castle-queue";

    private ObjectQueue<Event> eventObjectQueue;

    private Call<Void> flushCall;

    public EventQueue(Context context) {
        try {
            File file = new File(context.getApplicationContext().getFilesDir().getAbsoluteFile(), QUEUE_FILENAME);
            QueueFile queueFile = new QueueFile.Builder(file).build();
            eventObjectQueue = ObjectQueue.create(queueFile, new GsonConverter<>(Event.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void add(Event event) {
        try {
            eventObjectQueue.add(event);
        } catch (IOException e) {
            CastleLogger.e("Add to queue failed", e);
        }
    }

    private void trim() throws IOException {
        if (!isFlushing() && eventObjectQueue.size() > Castle.configuration().maxQueueLimit()) {
            int eventsToTrim = eventObjectQueue.size() - Castle.configuration().maxQueueLimit();
            CastleLogger.d("Trimming " + eventsToTrim + " events from queue");

            eventObjectQueue.remove(eventsToTrim);
        }
    }

    private boolean isFlushing() {
        return flushCall != null;
    }

    public synchronized void flush() throws IOException {
        CastleLogger.d("EventQueue size " + eventObjectQueue.size());
        if (flushCall == null && !eventObjectQueue.isEmpty() && eventObjectQueue.size() >= Castle.configuration().flushLimit()) {
            trim();

            CastleLogger.d("Flushing EventQueue " + eventObjectQueue.size());
            List<Event> events = eventObjectQueue.peek(Castle.configuration().flushLimit());

            Batch batch = new Batch();
            batch.addEvents(events);

            flushCall = CastleAPIService.getInstance().batch(batch);
            flushCall.enqueue(this);
        }
    }

    @Override
    public void onResponse(Call<Void> call, Response<Void> response) {
        if (response.isSuccessful()) {
            CastleLogger.i(response.code() + " " + response.message());
            CastleLogger.i("Batch request successful");

            try {
                eventObjectQueue.remove(Castle.configuration().flushLimit());
                CastleLogger.d("Removed " + Castle.configuration().flushLimit() + " events from EventQueue");
            } catch (IOException e) {
                e.printStackTrace();

                CastleLogger.e("Failed to remove events from queue");
            }
        } else {
            CastleLogger.e(response.code() + " " + response.message());
            try {
                CastleLogger.e("Batch request error :" + response.errorBody().string());
            } catch (IOException e) {
                e.printStackTrace();

                CastleLogger.e("Batch request error");
            }
        }
        flushCall = null;
    }

    @Override
    public void onFailure(Call<Void> call, Throwable t) {
        CastleLogger.e("Batch request failed", t);
        flushCall = null;
    }
}
