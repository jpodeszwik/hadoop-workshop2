package pl.isa.hadoop;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * Created by trener on 08.11.16.
 */
public class Sort {
    public static class WMapper extends Mapper<LongWritable, Text, LongWritable, Text> {
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] parts = value.toString().split("\\s+");
            long count = Long.valueOf(parts[1]);
            context.write(new LongWritable(count), new Text(parts[0]));
        }
    }

    public static class WReducer extends Reducer<LongWritable, Text, LongWritable, Text> {
        protected void reduce(LongWritable key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            for(Text letter : values) {
                context.write(key, letter);
            }
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        String input = args[0];
        String output = args[1];
        Job job = Job.getInstance();

        job.setMapperClass(WMapper.class);
        job.setReducerClass(WReducer.class);
        job.setJarByClass(Sort.class);
        job.setJobName("word count");
        job.setOutputKeyClass(LongWritable.class);
        job.setOutputValueClass(Text.class);


        FileInputFormat.addInputPath(job, new Path(input));
        FileOutputFormat.setOutputPath(job, new Path(output));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
