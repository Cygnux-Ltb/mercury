package io.mercury.serialization.kryo;

import java.io.File;
import java.util.Collection;

import io.mercury.common.serialization.specific.FileSerializer;

import javax.annotation.Nonnull;

public class KryoFileSerializer<T> implements FileSerializer<Collection<T>> {

    @Override
    public File serialization(@Nonnull Collection<T> source) {
        // TODO Auto-generated method stub
        return null;
    }

}
