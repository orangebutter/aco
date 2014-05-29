package org.oe.algorithm.mr;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.oe.algorithm.newant.Ant;
import org.oe.algorithm.newant.Parameter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zhou on 14-5-28.
 */
public class ACOMapReduce extends Configured implements Tool {
    private static double[][] distance;
    private static float[][] pheromone;
    private static int MAX_GEN;
    private static int cityNum;
    private static int antNum;
    private static float rho;
    private static int flag;
    private static float Q;
    private static float alpha;
    private static float beta;

    @Override
    public int run(String[] args) throws Exception {
//        String paramFile = args[2];
        String distanceFile = args[2];
        MAX_GEN = Integer.parseInt(args[3]);
//        readParameter(paramFile);
        readDistance(distanceFile);
        int mapCount = 1;
        String inputPath = args[0];
        //生成inputPath
        createInputFile(inputPath,mapCount);


        String outputPath = args[1];
        ACOMapReduce.pheromone = new float[cityNum][cityNum];
        for (int i = 0; i < cityNum; i++) {
            for (int j = 0; j < cityNum; j++) {
                ACOMapReduce.pheromone[i][j] = 0.1f;  //初始化为0.1
            }
        }

        for (int g = 0; g < MAX_GEN; g++) {
            JobConf job = new JobConf(ACOMapReduce.class);
            int antRaceNum = Parameter.AntNum/mapCount;
            job.set("antNum", antRaceNum + "");
            job.set("cityNum", cityNum + "");
            job.get("rho", Parameter.Rho + "");
            job.get("Q", Parameter.Q + "");
            job.get("flag", "1");
            job.get("alpha",  "1.5");
            job.get("beta", "5");
            job.setJobName("ACOMapReduce");
            job.setJarByClass(ACOMapReduce.class);
            job.setNumMapTasks(mapCount);

            job.setMapperClass(Map.class);                                //调用上面Map类作为Map任务代码
            job.setReducerClass(Reduce.class);                            //调用上面Reduce类作为Reduce任务代码
//        job.setOutputFormatClass(TextOutputFormat.class);

            job.setOutputKeyClass(LongWritable.class);
            job.setOutputValueClass(Text.class);

            job.setOutputFormat(TextOutputFormat.class);
            job.set("gen", g + "");
            outputPath = args[1] + "_" + g;
            FileInputFormat.addInputPath(job, new Path(inputPath));            //输入路径
            FileOutputFormat.setOutputPath(job, new Path(outputPath));        //输出路径
            RunningJob runningJob = JobClient.runJob(job);
            runningJob.waitForCompletion();
//            if (!runningJob.isSuccessful()) {
//                break;
//            }
            inputPath = outputPath;
            System.out.println("getOutputPath path name " + FileOutputFormat.getOutputPath(job).getName());
        }
        return 0;
        //   return job.isSuccessful() ? 0 : 1;
    }

    private void createInputFile(String inputPath, int mapCount) {
        String serverPath = "hdfs://192.168.1.40:54310/";
        Configuration conf = new Configuration();

        try {
            FileSystem hdfs = FileSystem.get(URI.create(serverPath), conf);
            FileSystem local = FileSystem.getLocal(conf);
            if(!local.exists(new Path(inputPath))){

                FSDataOutputStream out = null;
                try {
                    out = hdfs.create(new Path(inputPath));
                    for(int i=0;i<mapCount;i++){

                        String value =  String.format("%010d\t%010d\n",i,Integer.MAX_VALUE);
                        out.write(value.getBytes());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if(out!=null){
                        try {
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }



            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void readDistance(String filePath) {

        FileInputStream inputstream = null;
        InputStreamReader reader = null;
        BufferedReader br = null;
        List<String> data = new ArrayList<String>();
        try {
            inputstream = new FileInputStream(filePath);
            reader = new InputStreamReader(inputstream, "utf-8");

            br = new BufferedReader(reader);
            String line = null;
            int n = 0;

            while ((line = br.readLine()) != null) {
                n++;
                if (n > 7 && !line.startsWith("EOF")) {
                    data.add(line);
                }
            }

        } catch (IOException ex2) {
            ex2.printStackTrace();
        } finally {
            if (inputstream != null) {
                try {
                    inputstream.close();
                } catch (IOException ex) {
                }
            }

        }

        cityNum = data.size();
        distance = new double[cityNum][cityNum];
        for (int i = 0; i < cityNum - 1; i++) {
            distance[i][i] = 0;  //对角线为0
            double xi = Double.parseDouble(data.get(i).split(" ")[1]);
            double yi = Double.parseDouble(data.get(i).split(" ")[2]);

            for (int j = i + 1; j < cityNum; j++) {

                double xj = Double.parseDouble(data.get(j).split(" ")[1]);
                double yj = Double.parseDouble(data.get(j).split(" ")[2]);
                double rij = Math.sqrt(((xi - xj) * (xi - xj) + (yi - yj) * (yi - yj)));
                distance[i][j] = rij;
                distance[j][i] = distance[i][j];
            }
        }


    }

    private static void readParameter(String filePath) {


        FileInputStream inputstream = null;
        InputStreamReader reader = null;
        BufferedReader br = null;
        StringBuilder buffer = new StringBuilder();
        try {
            inputstream = new FileInputStream(filePath);
            reader = new InputStreamReader(inputstream, "utf-8");

            br = new BufferedReader(reader);
            String line = null;
            List<String> data = new ArrayList<String>();
            while ((line = br.readLine()) != null) {
                buffer.append(line);
            }
        } catch (IOException ex2) {
            ex2.printStackTrace();
        } finally {
            if (inputstream != null) {
                try {
                    inputstream.close();
                } catch (IOException ex) {
                }
            }
        }
        String[] tmp = buffer.toString().split(" ");

        MAX_GEN = Integer.parseInt(tmp[0]);
        antNum = Integer.parseInt(tmp[1]); //
        rho = Float.parseFloat(tmp[2]);
        flag = Integer.parseInt(tmp[3]);
        Q = Float.parseFloat(tmp[4]);
        alpha = Integer.parseInt(tmp[5]);
        beta = Float.parseFloat(tmp[6]);
    }

    public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, LongWritable, Text> {
        private Ant[] ants; //一群蚂蚁
        private int gen;
        private int cityNum;
        private int antNum;
        private double bestLength;
        public static int[] bestTour; //解路径


        @Override
        public void map(LongWritable key, Text value, OutputCollector<LongWritable, Text> outputCollector, Reporter reporter) throws IOException {
            String s_v = value.toString().split("\t")[1];
            System.out.println(" map pheromone " + ACOMapReduce.pheromone);
            long id = Long.parseLong(value.toString().split("\t")[0]);
            if(s_v.indexOf(":")>=0){
                bestLength = Integer.parseInt(s_v.toString().split(":")[0]);
            }else{
                bestLength = Integer.parseInt(s_v.toString());
            }

            for (int i = 0; i < antNum; i++) {
                for (int j = 1; j < cityNum; j++) {
                    ants[i].selectNextCity(ACOMapReduce.pheromone);//每只蚂蚁,根据信息素矩阵,完成对所有城市的遍历
                }
                ants[i].getTabu().add(ants[i].getFirstCity()); //蚂蚁最终回到出发的城市
                if (ants[i].getTourLength() < bestLength) { //用找到最优路径的蚂蚁设置解路径总长度
                    bestLength = ants[i].getTourLength();
                    for (int k = 0; k < cityNum + 1; k++) { //找到最优路径的蚂蚁,根据它禁忌表中节点添加的顺序,设置解路径
                        bestTour[k] = ants[i].getTabu().get(k).intValue();
                    }
                }
            }
            String outputText = "";
            for (int i = 0; i < bestTour.length; i++) {
                outputText += bestTour[i] + ",";
            }
            outputCollector.collect(new LongWritable(1), new Text(bestLength + ":" + outputText.substring(0, outputText.length() - 1)));
        }

        public void configure(org.apache.hadoop.mapred.JobConf job) {
            gen = Integer.parseInt(job.get("gen"));
            cityNum = Integer.parseInt(job.get("cityNum"));
            antNum = Integer.parseInt(job.get("antNum"));
            bestLength = Integer.MAX_VALUE;
            bestTour = new int[cityNum + 1];
            ants = new Ant[antNum];

            for (int i = 0; i < antNum; i++) {
                ants[i] = new Ant(cityNum);
                ants[i].init(distance, Parameter.alf, Parameter.bta);
            }
        }
    }

    /**
     * 统计同一个IMSI在同一时间段
     * 在不同CGI停留的时长
     */
    public static class Reduce extends MapReduceBase implements Reducer<LongWritable, Text, LongWritable, Text> {
        private int gen;
        private int flag;
        private int cityNum;
        private float Q;
        private float rho;
        private double[][] deltaPheromone;


        //更新信息素矩阵
        private void updatePheromone() {
            //信息素挥发
            for (int i = 0; i < cityNum; i++)
                for (int j = 0; j < cityNum; j++) {
                    pheromone[i][j] = pheromone[i][j] * (1 - rho) + 0.0001f; //rho是信息素挥发因子
                }
            //信息素添加
            for (int i = 0; i < cityNum; i++) {
                for (int j = 0; j < cityNum; j++) {
                    pheromone[i][j] += deltaPheromone[i][j];
                }
            }

        }

        //计算蚂蚁族的Delta值
        private void calculateAntDelta(HashSet<String> set) {
            for (int i = 0; i < cityNum; i++)
                for (int j = 0; j < cityNum; j++) {
                    deltaPheromone[i][j] = 0.000f; //rho是信息素挥发因子
                }
            int count = 0;
            for (Iterator<String> it = set.iterator(); it.hasNext(); ) {
                String value = it.next();
                int tourLength = Integer.parseInt(value.split(":")[0]);
                String[] cityIds = value.split(":")[1].split(",");
                for (int k = 0; k < cityIds.length - 1; k++) {
                    int currentCity = Integer.parseInt(cityIds[k]);
                    int nextCity = Integer.parseInt(cityIds[k + 1]);
                    if (flag == 1) {  //传统模型
                        deltaPheromone[currentCity][nextCity] += (float) ((1. / tourLength) * Q);
                        deltaPheromone[nextCity][currentCity] += (float) ((1. / tourLength) * Q);
                    } else if (flag == 0) { //蚁恒模型
                        double dis = distance[currentCity][nextCity];
                        deltaPheromone[currentCity][nextCity] += (float) (dis * (1. / tourLength) * Q);
                        deltaPheromone[nextCity][currentCity] += (float) (dis * (1. / tourLength) * Q);
                    }
                }
            }
        }

        @Override
        public void reduce(LongWritable text, Iterator<Text> textIterator, OutputCollector<LongWritable, Text> output, Reporter reporter) throws IOException {
            System.out.println(" reduce " + gen);
            HashSet set = new HashSet();
            int v_min = 0;
            String s_op = "";
            while (textIterator.hasNext()) {
                String value = textIterator.next().toString();
                if (Integer.parseInt(value.split(":")[0]) < v_min) {
                    v_min = Integer.parseInt(value.split(":")[0]);
                    s_op = value.split(":")[1];
                }
                set.add(value.split(":")[1]);
            }
            calculateAntDelta(set);
            updatePheromone();
            for (int i = 0; i < set.size(); i++) {
                output.collect(new LongWritable(i), new Text(v_min + ":" + s_op));
            }

            //更新Pheromone到一个文件中
        }


        public void configure(org.apache.hadoop.mapred.JobConf job) {
            //distance
//            int antNum = Integer.parseInt(job.get("ant_num")); //赋值
            gen = Integer.parseInt(job.get("gen"));
            rho = Integer.parseInt(job.get("rho"));
            flag = Integer.parseInt(job.get("flag"));
            Q = Integer.parseInt(job.get("Q"));
            cityNum = Integer.parseInt(job.get("cityNum"));
            //初始化distance
            //pheromone
        }
    }

    public static void main(String[] args) throws Exception {
        System.err.println(" args size " + args.length);
        for (int i = 0; i < args.length; i++) {
            System.err.println(" args  " + i + " value " + args[i]);
        }
//       System.out.println( String.format("%010d\t%010d\n",0,Integer.MAX_VALUE) );
//        readDistance("C:\\迅雷下载\\gr666.tsp\\gr666.tsp");
//        readParameter(ACOMapReduce.class.getResource("/parameter").getPath());
//        if (args.length != 4) {
//            System.err.println("");
//            System.err.println("Usage: BaseStationDataPreprocess < input path > < output path > < date > < timepoint >");
//            System.err.println("Example: BaseStationDataPreprocess /user/james/Base /user/james/Output 2012-09-12 07-09-17-24");
//            System.err.println("Warning: Timepoints should be begined with a 0+ two digit number and the last timepoint should be 24");
//            System.err.println("Counter:");
//            System.err.println("\t" + "TIMESKIP" + "\t" + "Lines which contain wrong date format");
//            System.err.println("\t" + "OUTOFTIMESKIP" + "\t" + "Lines which contain times that out of range");
//            System.err.println("\t" + "LINESKIP" + "\t" + "Lines which are invalid");
//            System.err.println("\t" + "USERSKIP" + "\t" + "Users in some time are invalid");
//            System.exit(-1);
//        }

        //运行任务
        int res = ToolRunner.run(new Configuration(), new ACOMapReduce(), args);

        System.exit(res);
    }
}
