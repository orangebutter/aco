package org.oe.algorithm.ant;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by Administrator on 14-3-27.
 */
public class Ant {
    private static final Log logger = LogFactory
            .getLog(Ant.class);
    int citys;
    int path[];
    int[] isvisitedcity;//是否访问过，取值是0或1，1表示访问过，0没访问过
    boolean isSelected=true; // 是否出线选择失败
    int startCityIndex = 0;
    public Ant(int citysum) {
        citys = citysum;
        startCityIndex = citys-1;
    }
    public void prevCityIndex() {//city选择向前一个
        startCityIndex--;
    }
    public void SelectFirstCity() {// 第一步
        // 清空数据
        isSelected = true;
        path = null;
        path = new int[citys];
        path[0] = startCityIndex;// 11

        isvisitedcity=null;
        isvisitedcity = new int[citys];
        isvisitedcity[startCityIndex] = 1;
    }

    /*
     * 选择下一个城市 index 选择第几个城市
     */
    public void SelectNextCity(int index, double[][] tao, int[][] distance) {// 下一步

        double[] p = new double[citys]; // 每只蚂蚁走向下一个城市的概率
        double alpha = 1.0; // 信息浓度参数
        double beta = 2.0; // 启发信息重要性参数
        double sum = 0; // 分母
        int currentcity = path[index - 1];// 上次的城市

        // 计算公式中的分母部分
        for (int i = 0; i < citys; i++) {
            if (isvisitedcity[i] == 0)
                if(distance[currentcity][i]!=0){
                    sum += (Math.pow(tao[currentcity][i], alpha) * Math.pow(
                            1.0 / distance[currentcity][i], beta));
                }
        }
        // 计算每个城市被选中的概率
        for (int i = 0; i < citys; i++) {
            if (isvisitedcity[i] == 1)
                p[i] = 0.0;
            else {
                if(distance[currentcity][i]==0){
                    p[i]=0.0;
                }else{
                    p[i] = (Math.pow(tao[currentcity][i], alpha) * Math.pow(
                            1.0 / distance[currentcity][i], beta)) / sum;
                }

            }


        }


        // 寻找下一个城市
        int selectct = -1;
        double p2 =0;
        int maxCount = citys * citys;
        int count =0;
        while (selectct == -1&& count<=maxCount) {
            count++;
            p2 = Math.random();
            logger.debug("生成的随机数:"+ p2);
            for (int i = 0; i < citys; i++) {
                if (isvisitedcity[i] == 0) {
                    if (1-p2 <= p[i]) {
                        selectct = i;
                        break;
                    }
                }
            }
        }

        if (selectct == -1) {
            logger.debug("select error");
            isSelected = false;
        } else {
            logger.debug("select poin:" + selectct);
            path[index] = selectct;// 11
            isvisitedcity[selectct] = 1;
        }

    }

    public int GetLength(int[][] distance) {
        int sumlength = 0;
        for (int i = 1; i < citys; i++) {
            sumlength += distance[path[i - 1]][path[i]];
        }
        return sumlength;
    }
}
