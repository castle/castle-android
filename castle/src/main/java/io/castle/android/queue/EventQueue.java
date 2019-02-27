/*
 * Copyright (c) 2017 Castle
 */

package io.castle.android.queue;

import android.content.Context;

import com.squareup.tape2.ObjectQueue;
import com.squareup.tape2.QueueFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import io.castle.android.Castle;
import io.castle.android.CastleLogger;
import io.castle.android.api.CastleAPIService;
import io.castle.android.api.model.Batch;
import io.castle.android.api.model.Event;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventQueue implements Callback<Void> {
    private static final String QUEUE_FILENAME = "castle-queue";
    private static final int MAX_BATCH_SIZE = 100;

    private ObjectQueue<Event> eventObjectQueue;

    private Call<Void> flushCall;
    private int flushCount;

    public EventQueue(Context context) {
        try {
            init(context);
        }
        catch (IOException e) {
            CastleLogger.e("Failed to create queue", e);

            // Delete the file and try again
            getFile(context).delete();

            try {
                init(context);
            } catch (IOException eRetry) {
                CastleLogger.e("Deleted queue file. Retried. Failed.", eRetry);
            }
        }
    }

    private File getFile(Context context) {
        return new File(context.getApplicationContext().getFilesDir().getAbsoluteFile(),
                        QUEUE_FILENAME);
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
        } catch (IOException e) {
            CastleLogger.e("Add to queue failed", e);
        }
    }

    private synchronized void trim() throws IOException {
        if (!isFlushing() && this.size() > Castle.configuration().maxQueueLimit()) {
            int eventsToTrim = this.size() - Castle.configuration().maxQueueLimit();
            eventObjectQueue.remove(eventsToTrim);
            CastleLogger.d("Trimmed " + eventsToTrim + " events from queue");
        }
    }

    public synchronized void flush() throws IOException {
        CastleLogger.d("EventQueue size " + eventObjectQueue.size());
        if (!isFlushing() && (!eventObjectQueue.isEmpty())) {
            trim();

            int end = Math.min(MAX_BATCH_SIZE, eventObjectQueue.size());
            List<Event> subList = new ArrayList<>(end);
            Iterator<Event> iterator = eventObjectQueue.iterator();
            for (int i = 0; i < end; i++) {
                Event event = iterator.next();
                if (event != null) {
                    subList.add(event);
                }
            }
            List<Event> events = Collections.unmodifiableList(subList);

            if (!events.isEmpty()) {
                Batch batch = new Batch();
                batch.addEvents(events);

                CastleLogger.d("Flushing EventQueue " + end);

                flushCount = end;
                flushCall = CastleAPIService.getInstance().batch(batch);
                flushCall.enqueue(this);
            } else {
                CastleLogger.d("Did not flush EventQueue ");
            }
        }
    }

    public synchronized boolean isFlushing() {
        return flushCall != null;
    }

    public synchronized boolean needsFlush() {
        return eventObjectQueue.size() >= Castle.configuration().flushLimit();
    }

    private synchronized void flushed() {
        flushCall = null;
        flushCount = 0;
    }

    public synchronized int size() {
        return eventObjectQueue.size();
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
                CastleLogger.d("Removed " + flushCount + " events from EventQueue");
            } catch (IOException e) {
                CastleLogger.e("Failed to remove events from queue", e);
            }

            // Check if queue size still exceed the flush limit and if it does, flush.
            if (needsFlush()) {
                Castle.flush();
            }
        } else {
            CastleLogger.e(response.code() + " " + response.message());
            try {
                CastleLogger.e("Batch request error:" + response.errorBody().string());
            } catch (Exception e) {
                CastleLogger.e("Batch request error", e);
            }
        }

        flushed();
    }

    @Override
    public void onFailure(Call<Void> call, Throwable t) {
        CastleLogger.e("Batch request failed", t);
        flushed();
    }

    public void destroy() {
        if (flushCall != null) {
            flushCall.cancel();
        }
        flushed();
    }
}
