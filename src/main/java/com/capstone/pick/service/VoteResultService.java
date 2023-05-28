package com.capstone.pick.service;

import com.capstone.pick.domain.Pick;
import com.capstone.pick.domain.VoteOption;
import com.capstone.pick.repository.PickRepository;
import com.capstone.pick.repository.VoteOptionRepository;
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
import java.util.StringTokenizer;

@Service
@RequiredArgsConstructor
public class VoteResultService {
    private final PickRepository pickRepository;
    private final VoteOptionRepository voteOptionRepository;

    public List<List<String>> getVoteResults(Long voteId) throws Exception {
        // 투표 선택지 불러오기
        List<VoteOption> voteOptions = voteOptionRepository.findAllByVoteId(voteId);

        // 하둡 입력(csv) 생성 - 투표 선택지 아이디_유저 정보 유형_유저 정보 값 (ex. 1_gender_female)
        String inputPath = System.getProperty("user.home") + File.separator + "input.csv";
        Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(inputPath), "UTF-8"));
        for (VoteOption vo : voteOptions) {
            for (Pick p : pickRepository.findAllByVoteOptionId(vo.getId())) {
                writer.write(vo.getId() + "_Gender_" + p.getUser().getGender() + "\n" +
                        vo.getId() + "_AgeRange_" + p.getUser().getAge_range() + "\n" +
                        vo.getId() + "_Address_" + (p.getUser().getAddress().split(" "))[0] + "\n" +
                        vo.getId() + "_Job_" + p.getUser().getJob() + "\n"
                );
            }
        }
        writer.close();

        // 하둡 설정
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "vote analysis");

        // MapReduce 작업을 위한 설정
        FileInputFormat.addInputPath(job, new Path(inputPath));
        job.setInputFormatClass(TextInputFormat.class);
        job.setMapperClass(TokenizerMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        job.setCombinerClass(IntSumReducer.class);
        job.setReducerClass(IntSumReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        // MapReduce 결과 출력 경로 설정
        Path outputPath = new Path(System.getProperty("user.home") + File.separator + "vote" + voteId + "_result");

        //파일 삭제
        FileSystem fs = FileSystem.get(conf);
        fs.delete(outputPath, true);

        FileOutputFormat.setOutputPath(job, outputPath);
        job.setOutputFormatClass(TextOutputFormat.class);

        // MapReduce 작업 실행
        job.waitForCompletion(true);

        // MapReduce 결과를 다시 CSV 파일로 저장
        String outputPathStr = System.getProperty("user.home") + File.separator + "result" + File.separator + "part-r-00000";
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(outputPathStr), "utf-8"));

        List<List<String>> result = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            String[] mr = line.split("\t");
            String[] col = mr[0].split("_");
            result.add(List.of(col[0], col[1], col[2],  mr[1]));
        }
        br.close();

        return result;
    }

    public static class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable> {
        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            StringTokenizer itr = new StringTokenizer(value.toString());
            if (itr.hasMoreTokens()) {
                word.set(itr.nextToken());
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

