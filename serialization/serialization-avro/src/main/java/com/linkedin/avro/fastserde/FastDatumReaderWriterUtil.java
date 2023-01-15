package com.linkedin.avro.fastserde;

import org.apache.avro.Schema;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Utility class to generate and reuse FastDatumReader/Writer. The cache key is
 * schema object.
 * <p>
 * Pre-requisite to use this util: 1. Schema object will not be changed
 * on-the-FLY, or the changes do NOT require new DatumReader/Writer; 2. Your
 * application will always use the same Schema object for the same schema;
 */
public class FastDatumReaderWriterUtil {

    protected static class SchemaPair {
        private final Schema writerSchema;
        private final Schema readerSchema;
        private final int hashCode;

        public SchemaPair(Schema writerSchema, Schema readerSchema) {
            this.writerSchema = writerSchema;
            this.readerSchema = readerSchema;
            this.hashCode = Objects.hash(System.identityHashCode(this.writerSchema),
                    System.identityHashCode(this.readerSchema));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            SchemaPair that = (SchemaPair) o;
            return writerSchema == that.writerSchema && readerSchema == that.readerSchema;
        }

        @Override
        public int hashCode() {
            return hashCode;
        }
    }

    private static final ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();

    // TODO : LRU cache
    private static final Map<SchemaPair, FastGenericDatumReader<?>> fastGenericDatumReaderCache = new FastAvroConcurrentHashMap<>();
    private static final Map<Schema, FastGenericDatumWriter<?>> fastGenericDatumWriterCache = new WeakIdentityHashMap<>();

    private static final Map<SchemaPair, FastSpecificDatumReader<?>> fastSpecificDatumReaderCache = new FastAvroConcurrentHashMap<>();
    private static final Map<Schema, FastSpecificDatumWriter<?>> fastSpecificDatumWriterCache = new WeakIdentityHashMap<>();

    private FastDatumReaderWriterUtil() {
    }

    public static <T> FastGenericDatumReader<T> getFastGenericDatumReader(Schema schema) {
        return getFastGenericDatumReader(schema, schema);
    }

    @SuppressWarnings("unchecked")
    public static <T> FastGenericDatumReader<T> getFastGenericDatumReader(Schema writerSchema, Schema readerSchema) {
        SchemaPair schemaPair = new SchemaPair(writerSchema, readerSchema);
        return (FastGenericDatumReader<T>) fastGenericDatumReaderCache.computeIfAbsent(schemaPair,
                key -> new FastGenericDatumReader<>(writerSchema, readerSchema));
    }

    @SuppressWarnings("unchecked")
    public static <T> FastGenericDatumWriter<T> getFastGenericDatumWriter(Schema writerSchema) {
        FastGenericDatumWriter<T> fastDatumWriter;

        // lookup cache and read lock
        reentrantReadWriteLock.readLock().lock();
        try {
            fastDatumWriter = (FastGenericDatumWriter<T>) fastGenericDatumWriterCache.get(writerSchema);
        } finally {
            reentrantReadWriteLock.readLock().unlock();
        }
        // update cache and write lock
        if (fastDatumWriter == null) {
            reentrantReadWriteLock.writeLock().lock();
            try {
                fastDatumWriter = new FastGenericDatumWriter<>(writerSchema);
                fastGenericDatumWriterCache.put(writerSchema, fastDatumWriter);
            } finally {
                reentrantReadWriteLock.writeLock().unlock();
            }
        }
        return fastDatumWriter;
    }

    public static <T> FastSpecificDatumReader<T> getFastSpecificDatumReader(Schema schema) {
        return getFastSpecificDatumReader(schema, schema);
    }

    @SuppressWarnings("unchecked")
    public static <T> FastSpecificDatumReader<T> getFastSpecificDatumReader(Schema writerSchema, Schema readerSchema) {
        SchemaPair schemaPair = new SchemaPair(writerSchema, readerSchema);
        return (FastSpecificDatumReader<T>) fastSpecificDatumReaderCache.computeIfAbsent(schemaPair,
                key -> new FastSpecificDatumReader<>(writerSchema, readerSchema));
    }

    @SuppressWarnings("unchecked")
    public static <T> FastSpecificDatumWriter<T> getFastSpecificDatumWriter(Schema writerSchema) {
        FastSpecificDatumWriter<T> fastDatumWriter;

        // lookup cache and read lock
        reentrantReadWriteLock.readLock().lock();
        try {
            fastDatumWriter = (FastSpecificDatumWriter<T>) fastSpecificDatumWriterCache.get(writerSchema);
        } finally {
            reentrantReadWriteLock.readLock().unlock();
        }
        // update cache and write lock
        if (fastDatumWriter == null) {
            reentrantReadWriteLock.writeLock().lock();
            try {
                fastDatumWriter = new FastSpecificDatumWriter<>(writerSchema);
                fastSpecificDatumWriterCache.put(writerSchema, fastDatumWriter);
            } finally {
                reentrantReadWriteLock.writeLock().unlock();
            }
        }
        return fastDatumWriter;
    }
}
