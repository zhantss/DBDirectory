package pro.zhantss.lucene.store.database.index;

import java.io.IOException;
import java.io.InputStream;

import org.apache.lucene.store.BufferedIndexInput;
import org.apache.lucene.store.IOContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pro.zhantss.lucene.store.database.DatabaseDirectory;
import pro.zhantss.lucene.store.database.DatabaseDirectoryException;
import pro.zhantss.lucene.store.database.handler.DatabaseDirectoryHandler;

public class DatabaseIndexInput extends BufferedIndexInput {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseFetchIndexInput.class);
    private static final DatabaseDirectoryHandler handler = DatabaseDirectoryHandler.INSTANCE;

    private final DatabaseDirectory directory;
    private final String name;
    private InputStream stream;
    private final long length;
    private int pos = 0;

    public DatabaseIndexInput(final DatabaseDirectory directory, final String name, final IOContext context)
            throws DatabaseDirectoryException {
        super(name, context);
        this.directory = directory;
        this.name = name;
        length = handler.fileLength(directory, name);
        stream = length > 0l ? handler.fileStream(directory, name) : null;
    }

    @Override
    protected void readInternal(final byte[] b, final int offset, final int length) throws IOException {
        LOGGER.trace("{}.readInternal({}, {}, {})", this, b, offset, length);
        stream.reset();
        stream.skip(pos);
        final byte[] aux = new byte[length];
        stream.read(aux, 0, length);
        System.arraycopy(aux, 0, b, offset, length);
        pos += length;
    }

    @Override
    protected void seekInternal(final long pos) throws IOException {
        LOGGER.trace("{}.seekInternal({})", this, pos);
        this.pos = (int) pos;
    }

    @Override
    public void close() throws IOException {
        LOGGER.trace("{}.close()", this);
        if (stream != null) {
        	stream.close();
        	stream = null;
        }
    }

    @Override
    public long length() {
        LOGGER.trace("{}.length()", this);
        return length;
    }

    @Override
    public String toString() {
        return new StringBuilder().append(this.getClass().getSimpleName()).append(":").append(directory).append("/")
                .append(name).toString();
    }
}
