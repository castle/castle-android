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
    private int flushCount;

    public EventQueue(Context context) {
        try {
            init(context);
        } catch (IOException e) {
            e.printStackTrace();

            // Delete the file and try again
            getFile(context).delete();
            try {
                init(context);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private File getFile(Context context) {
        return new File(context.getApplicationContext().getFilesDir().getAbsoluteFile(), QUEUE_FILENAME);
    }

    private void init(Context context) throws IOException {
        File file = getFile(context);
        QueueFile queueFile = new QueueFile.Builder(file).build();
        eventObjectQueue = ObjectQueue.create(queueFile, new GsonConverter<>(Event.class));
    }

    public void add(Event event) {
        try {
            synchronized (eventObjectQueue) {
                eventObjectQueue.add(event);
            }
        } catch (Exception e) {
            CastleLogger.e("Add to queue failed", e);
        }
    }

    private void trim() throws IOException {
        if (!isFlushing() && eventObjectQueue.size() > Castle.configuration().maxQueueLimit()) {
            int eventsToTrim = eventObjectQueue.size() - Castle.configuration().maxQueueLimit();
            CastleLogger.d("Trimming " + eventsToTrim + " events from queue");

            synchronized (eventObjectQueue) {
                eventObjectQueue.remove(eventsToTrim);
            }
        }
    }

    public boolean isFlushing() {
        return flushCall != null;
    }

    public synchronized void flush() throws IOException {
        CastleLogger.d("EventQueue size " + eventObjectQueue.size());
        if (!isFlushing() && (!eventObjectQueue.isEmpty())) {
            trim();

            List<Event> events = eventObjectQueue.peek(Castle.configuration().flushLimit());

            Batch batch = new Batch();
            batch.addEvents(events);

            CastleLogger.d("Flushing EventQueue " + events.size());

            flushCount = events.size();
            flushCall = CastleAPIService.getInstance().batch(batch);
            flushCall.enqueue(this);
        }
    }

    public boolean needsFlush() {
        return eventObjectQueue.size() >= Castle.configuration().flushLimit();
    }

    @Override
    public void onResponse(Call<Void> call, Response<Void> response) {
        if (response.isSuccessful()) {
            CastleLogger.i(response.code() + " " + response.message());
            CastleLogger.i("Batch request successful");

            try {
                synchronized (eventObjectQueue) {
                    eventObjectQueue.remove(flushCount);
                }
                CastleLogger.d("Removed " + flushCount+ " events from EventQueue");
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
        flushCount = 0;
        flushCall = null;
    }

    @Override
    public void onFailure(Call<Void> call, Throwable t) {
        CastleLogger.e("Batch request failed", t);
        flushCount = 0;
        flushCall = null;
    }

    public int size() {
        return eventObjectQueue.size();
    }
}
