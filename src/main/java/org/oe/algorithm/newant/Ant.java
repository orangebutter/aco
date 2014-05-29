package org.oe.algorithm.newant;

import java.util.Random;
import java.util.Vector;

/**
 * @author orange
 */
public class Ant implements Cloneable {

    private Vector<Integer> tabu; //禁忌表
    private Vector<Integer> allowedCities; //允许搜索的城市
    private float[][] delta; //信息素变化矩阵:一只蚂蚁,在一次遍历中,为经过的路段增加的信息素
    private double[][] distance; //距离矩阵

    private float alpha;
    private float beta;

    private double tourLength; //路径长度
    private int cityNum; //城市数量

    private int firstCity; //起始城市
    private int currentCity; //当前城市

    public Ant() {
        cityNum = 48;
        tourLength = 0;

    }

    public Ant(int num) {
        cityNum = num;
        tourLength = 0;
    }

    /**
     * 初始化设置阿尔法、β、距离矩阵、禁忌表、可访问表、信息素变化矩阵->
     * 随机设置一个起点城市
     */
    public void init(double[][] distance, float a, float b) {
    //根据传入参数设置阿尔法、β、距离矩阵,初始化禁忌表、可访问表、信息素变化矩阵
        alpha = a;
        beta = b;
        allowedCities = new Vector<Integer>();
        tabu = new Vector<Integer>();
        this.distance = distance;
        delta = new float[cityNum][cityNum];
        //初始状态下,设置可访问表包含每一座城市、信息素变化矩阵每个元素都为0
        for (int i = 0; i < cityNum; i++) {
            Integer integer = new Integer(i);
            allowedCities.add(integer);
            for (int j = 0; j < cityNum; j++) {
                delta[i][j] = 0.f;
            }
        }
        //随机设置起点城市
        Random random = new Random(System.currentTimeMillis());
        firstCity = random.nextInt(cityNum);
        //将起点城市从可访问表删除,添加到禁忌表
        for (Integer i : allowedCities) {
            if (i.intValue() == firstCity) {
                allowedCities.remove(i);
                break;
            }
        }
        tabu.add(Integer.valueOf(firstCity));
        currentCity = firstCity;
    }

    /**
     * 选择下一个城市:
     * 计算概率->
     * 轮盘赌选择下一个城市->
     * 更新可访问表、禁忌表、当前城市
     *
     * @param pheromone 信息素矩阵
     */
    public void selectNextCity(float[][] pheromone) {
        float[] p = new float[allowedCities.capacity()];
        float sum = 0.0f;
        int count = 0;
        int max;
        //计算分母部分
        for (Integer i : allowedCities) {
            sum += Math.pow(pheromone[currentCity][i.intValue()],
                    alpha) * Math.pow(1.0 / distance[currentCity][i.intValue()], beta);
        }
        //计算概率矩阵
        for (Integer j : allowedCities) {
            p[count] = (float) (Math.pow(pheromone[currentCity][j.intValue()],
                    alpha) * Math.pow(1.0 / distance[currentCity][j.intValue()], beta)) / sum;
            count++;
        }

        //轮盘赌选择下一个城市
        Random random = new Random(System.currentTimeMillis());
        float sleectP = random.nextFloat();
        int selectCity = 0;
        float sum1 = 0.f;
        for (int i = 0; i < allowedCities.capacity(); i++) {
            sum1 += p[i];
            if (sum1 >= sleectP) {
                selectCity = i;
                break;
            }
        }
        count = 0;
        for (Integer j : allowedCities) {
            if (count == selectCity) {
                selectCity = j;
                break;
            }
            count++;
        }

        //从允许选择的城市中去除select city
        for (Integer i : allowedCities) {
            if (i.intValue() == selectCity) {
                allowedCities.remove(i);
                break;
            }
        }
        //在禁忌表中添加select city
        tabu.add(Integer.valueOf(selectCity));
        //将当前城市改为选择的城市
        currentCity = selectCity;

    }

    /**
     * 计算这只蚂蚁本次遍历经过的路径长度
     */
    private double calculateTourLength() {
        double len = 0;
        for (int i = 0; i < cityNum; i++) {
            len += distance[this.tabu.get(i).intValue()][this.tabu.get(i + 1).intValue()];
        }
        return len;
    }


    public Vector<Integer> getAllowedCities() { //返回可访问表
        return allowedCities;
    }

    public void setAllowedCities(Vector<Integer> allowedCities) { //设置可访问表
        this.allowedCities = allowedCities;
    }

    public double getTourLength() { //返回这只蚂蚁本次遍历经过的路径长度
        tourLength = calculateTourLength();
        return tourLength;
    }

    public void setTourLength(int tourLength) { //设置这只蚂蚁本次遍历经过的路径长度
        this.tourLength = tourLength;
    }

    public int getCityNum() { //返回城市数目
        return cityNum;
    }

    public void setCityNum(int cityNum) { //设置城市数目
        this.cityNum = cityNum;
    }

    public Vector<Integer> getTabu() { //返回禁忌表
        return tabu;
    }

    public void setTabu(Vector<Integer> tabu) { //设置禁忌表
        this.tabu = tabu;
    }

    public float[][] getDelta() { //返回信息素变化矩阵
        return delta;
    }

    public void setDelta(float[][] delta) { //设置信息素变化矩阵
        this.delta = delta;
    }

    public int getFirstCity() { //返回起点城市
        return firstCity;
    }

    public void setFirstCity(int firstCity) { //设置起点城市
        this.firstCity = firstCity;
    }

}
