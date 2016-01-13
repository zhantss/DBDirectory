package pro.zhantss.lucene.store.database.index;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import org.apache.lucene.store.BufferedIndexOutput;
import org.apache.lucene.store.IOContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pro.zhantss.lucene.data.handler.DataHandler;
import pro.zhantss.lucene.store.database.DatabaseDirectory;
import pro.zhantss.lucene.store.database.DatabaseDirectoryException;
import pro.zhantss.lucene.store.database.handler.DatabaseDirectoryHandler;

/**
 * An <code>IndexOutput</code> implemenation that writes all the data to a
 * temporary file, and when closed, flushes the file to the database.
 * <p/>
 * Usefull for large files that are known in advance to be larger then the
 * acceptable threshold configured.
 *
 */
public class DatabaseFileIndexOutput extends BufferedIndexOutput {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(DatabaseFileIndexOutput.class);
	private static final DatabaseDirectoryHandler handler = DatabaseDirectoryHandler.INSTANCE;
	
	private static final int CHUNK_SIZE = 8192;

	private final String name;
	private final DatabaseDirectory directory;
	@SuppressWarnings("unused")
	private final IOContext context;

	private final RandomAccessFile file;
	private final File tempFile;
	private final Checksum digest = new CRC32();
	private long pos = 0;

	public DatabaseFileIndexOutput(final DatabaseDirectory directory,
			final String name, final IOContext context)
					throws DatabaseDirectoryException {
		super(CHUNK_SIZE);
		this.directory = directory;
		this.name = name;
		this.context = context;
		try {
			// 创建临时文件
			tempFile = File.createTempFile(directory.getIndexTableName() + "_"
					+ name + "_" + System.currentTimeMillis(), ".ljt");
			file = new RandomAccessFile(tempFile, "rw");
		} catch (final IOException e) {
			throw new DatabaseDirectoryException(e);
		}
	}

	@Override
	public long getFilePointer() {
		LOGGER.trace("{}.getFilePointer()", this);
		return pos;
	}

	public long getChecksum() throws IOException {
		LOGGER.trace("{}.getChecksum()", this);
		return digest.getValue();
	}

	@Override
	public void writeByte(final byte b) throws IOException {
		LOGGER.trace("{}.writeByte({})", this, b);
		file.write(b);
		digest.update(b);
		pos++;
	}

	@Override
	public void writeBytes(final byte[] b, final int offset, final int length)
			throws IOException {
		LOGGER.trace("{}.writeBytes({}, {}, {})", this, b, offset, length);
		file.write(b, offset, length);
		digest.update(b, offset, length);
		pos += length;
	}

	@Override
	public void close() throws IOException {
		LOGGER.trace("{}.close()", this);
		// file.seek(0);
		seek(0);	// Lucene 4.7
		final InputStream stream = new BufferedInputStream(
				new FileInputStream(file.getFD()));
		// 兼容性修改, 读取出来再存入
		byte[] content = DataHandler.streamToByteListAndClose(stream);
		//handler.saveFile(directory, name, stream, file.length());
		handler.saveFile(directory, name, content, content.length);
		file.close();
		tempFile.delete();
	}

	@Override
	public String toString() {
		return new StringBuilder().append(this.getClass().getSimpleName())
				.append(":").append(directory).append("/").append(name)
				.toString();
	}
	
	// TODO 测试
	// 以下代码均为测试
	
	@Override
	public void seek(long pos) throws IOException {
		super.seek(pos);
		file.seek(pos);
	}

	@Override
	public long length() throws IOException {
		return file.length();
	}

	@Override
	protected void flushBuffer(byte[] b, int offset, int size)
			throws IOException {
		while (size > 0) {
			final int toWrite = Math.min(CHUNK_SIZE, size);
			file.write(b, offset, toWrite);
			offset += toWrite;
			size -= toWrite;
		}
	}
}
