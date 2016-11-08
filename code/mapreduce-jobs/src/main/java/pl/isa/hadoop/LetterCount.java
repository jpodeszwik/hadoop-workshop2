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
public class LetterCount {
    public static class WMapper extends Mapper<LongWritable, Text, Text, LongWritable> {
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            char[] chars = value.toString().toCharArray();

            for(char c : chars) {
                context.write(new Text(String.valueOf(c)), new LongWritable(1));
            }
        }
    }

    public static class WReducer extends Reducer<Text, LongWritable, Text, LongWritable> {
        protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
            long sum = 0;

            for(LongWritable lw : values) {
                sum += lw.get();
            }

            context.write(key, new LongWritable(sum));
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        String input = args[0];
        String output = args[1];
        Job job = Job.getInstance();

        job.setMapperClass(WMapper.class);
        job.setReducerClass(WReducer.class);
        job.setJarByClass(LetterCount.class);
        job.setJobName("word count");
        job.setOutputKeyClass(Text.class);
        job.setNumReduceTasks(2);
        job.setOutputValueClass(LongWritable.class);


        FileInputFormat.addInputPath(job, new Path(input));
        FileOutputFormat.setOutputPath(job, new Path(output));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
