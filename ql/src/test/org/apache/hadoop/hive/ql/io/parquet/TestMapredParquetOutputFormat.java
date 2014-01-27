package org.apache.hadoop.hive.ql.io.parquet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.Properties;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.ql.io.parquet.write.DataWritableWriteSupport;
import org.apache.hadoop.hive.ql.io.parquet.write.ParquetRecordWriterWrapper;
import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.util.Progressable;
import org.junit.Test;

import parquet.hadoop.ParquetOutputFormat;

/**
 * Tests for MapredParquetOutputFormat.
 *
 * @author Justin Coffey <j.coffey@criteo.com>
 *
 */
public class TestMapredParquetOutputFormat {

  @Test
  public void testConstructor() {
    new MapredParquetOutputFormat();
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testConstructorWithFormat() {
    new MapredParquetOutputFormat((ParquetOutputFormat<ArrayWritable>) mock(ParquetOutputFormat.class));
  }

  @Test
  public void testGetRecordWriterThrowsException() {
    try {
      new MapredParquetOutputFormat().getRecordWriter(null, null, null, null);
      fail("should throw runtime exception.");
    } catch (Exception e) {
      assertEquals("Should never be used", e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testGetHiveRecordWriter() throws IOException {
    Properties tableProps = new Properties();
    tableProps.setProperty("columns", "foo,bar");
    tableProps.setProperty("columns.types", "int:int");

    final Progressable mockProgress = mock(Progressable.class);
    final ParquetOutputFormat<ArrayWritable> outputFormat = (ParquetOutputFormat<ArrayWritable>) mock(ParquetOutputFormat.class);

    JobConf jobConf = new JobConf();

    try {
      new MapredParquetOutputFormat(outputFormat) {
        @Override
        protected ParquetRecordWriterWrapper getParquerRecordWriterWrapper(
            ParquetOutputFormat<ArrayWritable> realOutputFormat,
            JobConf jobConf,
            String finalOutPath,
            Progressable progress
            ) throws IOException {
          assertEquals(outputFormat, realOutputFormat);
          assertNotNull(jobConf.get(DataWritableWriteSupport.PARQUET_HIVE_SCHEMA));
          assertEquals("/foo", finalOutPath.toString());
          assertEquals(mockProgress, progress);
          throw new RuntimeException("passed tests");
        }
      }.getHiveRecordWriter(jobConf, new Path("/foo"), null, false, tableProps, mockProgress);
      fail("should throw runtime exception.");
    } catch (RuntimeException e) {
      assertEquals("passed tests", e.getMessage());
    }
  }
}
