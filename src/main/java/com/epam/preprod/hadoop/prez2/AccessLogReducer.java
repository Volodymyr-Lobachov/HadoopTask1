package com.epam.preprod.hadoop.prez2;

import com.epam.preprod.hadoop.extension.PairOutputWritable;
import com.epam.preprod.hadoop.extension.PairWritable;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.lib.db.DBWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * Created by Volodymyr_Lobachov on 11/3/2015.
 */
public class AccessLogReducer extends Reducer<Text, PairWritable, Text, PairOutputWritable> {

    private LongWritable sum = new LongWritable(0);
    private DoubleWritable avg = new DoubleWritable(0);
    private long counter;
    private PairOutputWritable outputValue = new PairOutputWritable();

    @Override
    protected void reduce(Text key, Iterable<PairWritable> values, Context context) throws IOException, InterruptedException {
        initialize();
        for (PairWritable data : values) {
            sum.set(sum.get() + data.getFirst().get());
            counter += data.getSecond().get();
        }
        avg.set((double) sum.get() / counter);


        outputValue.setAvg(avg);
        outputValue.setSum(sum);

        context.write(key, outputValue);
    }

    private void initialize() {
        counter = 0;
        sum.set(0);
        avg.set(0);
    }
}
