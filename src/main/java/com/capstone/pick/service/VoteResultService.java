package com.capstone.pick.service;

import com.capstone.pick.domain.Pick;
import com.capstone.pick.domain.VoteOption;
import com.capstone.pick.repository.PickRepository;
import com.capstone.pick.repository.VoteOptionRepository;
import com.capstone.pick.util.VoteAnalysisParser;
import lombok.RequiredArgsConstructor;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VoteResultService {

    private final PickRepository pickRepository;
    private final VoteOptionRepository voteOptionRepository;

    public List<List<String>> getVoteResults(Long voteId) throws Exception {
        List<VoteOption> voteOptions = voteOptionRepository.findAllByVoteId(voteId);
        String filename = voteId + "_" + voteOptions.get(0).getVote().getTitle();
        List<List<String>> result = new ArrayList<>();
        result.add(List.of(String.valueOf(voteId), voteOptions.get(0).getVote().getTitle()));

        Configuration conf = new Configuration();
        for (VoteOption vo : voteOptions) {
            String inputPath = "/tmp/" + filename + ".csv";
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(inputPath), "UTF-8"));
            for (Pick p : pickRepository.findAllByVoteOptionId(vo.getId())) {
                writer.write(vo.getId() + "," +
                        vo.getContent() + "," +
                        p.getUser().getGender() + "," +
                        p.getUser().getAge_range() + "," +
                        p.getUser().getAddress() + "," +
                        p.getUser().getJob() + "\n"
                );
            }
            writer.close();

            result.addAll(runMapreduce(conf, inputPath, vo.getId(), vo.getContent(), "gender", TokenizerMapper_gender.class));
            result.addAll(runMapreduce(conf, inputPath, vo.getId(), vo.getContent(), "age_range", TokenizerMapper_ageRange.class));
            result.addAll(runMapreduce(conf, inputPath, vo.getId(), vo.getContent(), "address", TokenizerMapper_address.class));
            result.addAll(runMapreduce(conf, inputPath, vo.getId(), vo.getContent(), "job", TokenizerMapper_job.class));

            // 임시 파일 삭제
            FileSystem fs = FileSystem.get(conf);
            fs.delete(new Path(inputPath), true);
        }

//        String csvOutputPath = "/tmp/" + filename + "_analysis.csv";
//        CSVWriter csvWriter = new CSVWriter(new OutputStreamWriter(new FileOutputStream(csvOutputPath), "euc-kr"));
//        csvWriter.writeAll(result);
//        csvWriter.close();

        return result;
    }

    public List<List<String>> runMapreduce(Configuration conf, String inputPath, Long optionId , String optionContent, String column, Class mapper) throws Exception {
        // 하둡 설정을 로드
        Job job = Job.getInstance(conf, "vote analysis");

        // 입력 데이터를 분할하여 맵 태스크에서 병렬 처리할 수 있도록 함
        FileInputFormat.addInputPath(job, new Path(inputPath));
        job.setInputFormatClass(TextInputFormat.class);

        // MapReduce 작업을 위한 설정
        job.setMapperClass(mapper);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setCombinerClass(IntSumReducer.class);
        job.setReducerClass(IntSumReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        // 출력 경로 설정
        Path outputPath = new Path("/tmp/result");
        FileOutputFormat.setOutputPath(job, outputPath);
        job.setOutputFormatClass(TextOutputFormat.class);

        // MapReduce 작업 실행
        job.waitForCompletion(true);

        // 맵리듀스 결과를 CSV 파일로 저장
        String outputPathStr = "/tmp/result/part-r-00000";

        List<List<String>> result = new ArrayList<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(outputPathStr), "euc-kr"));
        String line;
        while ((line = br.readLine()) != null) {
            String[] split = line.split("\t");
            result.add(List.of(String.valueOf(optionId), optionContent, column, split[0], split[1]));
        }
        br.close();

        // 임시 파일 삭제
        FileSystem fs = FileSystem.get(conf);
        fs.delete(outputPath, true);

        return result;
    }

    public static class TokenizerMapper_gender extends Mapper<Object, Text, Text, IntWritable> {
        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            VoteAnalysisParser parser = new VoteAnalysisParser(value);
            if (!parser.getGender().isEmpty()) {
                word.set(parser.getGender());
                context.write(word, one);
            }
        }
    }

    public static class TokenizerMapper_ageRange extends Mapper<Object, Text, Text, IntWritable> {
        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            VoteAnalysisParser parser = new VoteAnalysisParser(value);
            if (!parser.getAge_range().isEmpty()) {
                word.set(parser.getAge_range());
                context.write(word, one);
            }
        }
    }

    public static class TokenizerMapper_address extends Mapper<Object, Text, Text, IntWritable> {
        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            VoteAnalysisParser parser = new VoteAnalysisParser(value);
            if (!parser.getAddress().isEmpty()) {
                word.set(parser.getAddress());
                context.write(word, one);
            }
        }
    }

    public static class TokenizerMapper_job extends Mapper<Object, Text, Text, IntWritable> {
        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            VoteAnalysisParser parser = new VoteAnalysisParser(value);
            if (!parser.getJob().isEmpty()) {
                word.set(parser.getJob());
                context.write(word, one);
            }
        }
    }

    public static class IntSumReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        private IntWritable result = new IntWritable();

        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            result.set(sum);
            context.write(key, result);
        }
    }
}

