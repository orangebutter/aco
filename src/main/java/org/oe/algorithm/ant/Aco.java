package org.oe.algorithm.ant;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by Administrator on 14-3-27.
 */
public class Aco {
    private static final Log logger = LogFactory
            .getLog(Aco.class);
    private int antcount = 0; //蚂蚁数量
    private int citycount = 0; //城市数量
    private int runtime = 0;  //执行次数
    private double[][] tao;// 信息素矩阵//表示城市i到城市j的信息素
    private int[][] dis;// 距离矩阵//表示城市i到城市j的距离
    private int bestlength;// 求的最优解的长度
    private int[] besttour;// 求解的最佳路径

    Ant[] ants = null;
    int maxCount=1;

    public void init(int[][] distance, int antcount, int runtime) {
        dis = distance;
        // 初始化蚂蚁数量
        citycount = dis.length;// 城市数量
        this.antcount = antcount;
        this.runtime = runtime;

        besttour = new int[citycount];
        bestlength = 0;

        //初始化信息素矩阵
        tao = new double[citycount][citycount];// 信息素矩阵=城市数量*城市数量
        for(int i=0;i<citycount;i++)  {
            for(int j=0;j<citycount;j++){
                tao[i][j]=0.1;
                while(true){
                    if(maxCount<distance[i][j]){
                        maxCount *=10;
                    }else{
                        break;
                    }
                }
            }
        }

        // 初始化蚂蚁数量
        ants = new Ant[antcount];
        for (int i = 0; i < antcount; i++) {
            ants[i] = new Ant(citycount);
        }
    }

    public void run() {
        int startIndex = citycount-1;
        for (int g = 0; g < runtime; g++) {
           logger.debug("代数:" + g);
            for (int i = 0; i < ants.length; i++) {// 每只蚂蚁的操作
                // 初始化每只蚂蚁的起点
                if(!ants[i].isSelected){
                    ants[i].prevCityIndex();
                }
                ants[i].SelectFirstCity();

                // 走向其它城市
                for (int j = 1; j < citycount; j++) {
                    if(!ants[i].isSelected){
                        break;
                    }
                    ants[i].SelectNextCity(j, tao, dis);
                }

                int sumlength = ants[i].GetLength(dis);

                if (sumlength > bestlength) {
                    // 保存最优解
                    bestlength = sumlength;

                    for (int j = 0; j < citycount ; j++)
                        besttour[j] = ants[i].path[j];
                }

            }

            // 更新信息素
            updataTao();
        }
    }

    private void updataTao() {
        double rou = 0.5; // 信息素挥发速度
        //信息素挥发
        for (int i = 0; i < citycount; i++)
            for (int j = 0; j < citycount; j++)
                tao[i][j] = tao[i][j] * (1 - rou);

        //信息素更新(累加)
        for (int i = 0; i < antcount; i++) {
            for (int j = 1; j < citycount; j++) {
                // 长度越长，信息素越微弱
                tao[ants[i].path[j-1]][ants[i].path[j]] += ants[i].GetLength(dis)/maxCount;
            }
        }
    }

    public int[] getBesttour() {
        return besttour;
    }

    public void showresult() {
        System.out.println("\n最长路程是：" + bestlength);
        System.out.println("最长路径是：");
        for (int i = 0; i < besttour.length; i++) {
            System.out.println(besttour[i] + ">>");
        }

        System.out.println("\n");

        for (int i = 0; i < ants.length; i++) {
            System.out.print("蚂蚁" + i+"的路径:");
            for (int j:ants[i].path) {
                System.out.print(j+">>");
            }
            System.out.println();
        }
    }
}
