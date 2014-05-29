package org.oe.algorithm.newant;

/**
 * Created by zhou on 14-5-27.
 */
/**
 * 24
 * Data类用于保存城市的位置信息。
 * data属性是二维数组对于每一行元素第一个值是城市id第二个值是此城市横坐标第三
 * 个值是纵坐标
 * getdata()方法用于返回某一指定元素的值即指定城市的横坐标或纵坐标
 *
 * @author 彤
 */
public class Data {
    public static int dataLength = 42;
    public static double[][] data = {{1, 170.0, 85.0},
            {2, 166.0, 88.0}, {3, 133.0, 73.0}, {4, 140.0, 70.0}, {5, 142.0, 55.0}, {6, 126.0, 53.0},
            {7, 125.0, 60.0}, {8, 119.0, 68.0}, {9, 117.0, 74.0}, {10, 99.0, 83.0}, {11, 73.0, 79.0},
            {12, 72.0, 91.0}, {13, 37.0, 94.0}, {14, 6.0, 106.0}, {15, 3.0, 97.0}, {16, 21.0, 82.0},
            {17, 33.0, 67.0}, {18, 4.0, 66.0}, {19, 3.0, 42.0}, {20, 27.0, 33.0}, {21, 52.0, 41.0},
            {22, 57.0, 59.0}, {23, 58.0, 66.0}, {24, 88.0, 65.0}, {25, 99.0, 67.0}, {26, 95.0, 55.0},
            {27, 89.0, 55.0}, {28, 83.0, 38.0}, {29, 85.0, 25.0}, {30, 104.0, 35.0}, {31, 112.0, 37.0},
            {32, 112.0, 24.0}, {33, 113.0, 13.0}, {34, 125.0, 30.0}, {35, 135.0, 32.0}, {36, 147.0, 18.0},
            {37, 147.5, 36.0}, {38, 154.5, 45.0}, {39, 157.0, 54.0}, {40, 158.0, 61.0}, {41, 172.0, 82.0},
            {42, 174.0, 87.0}};

    public static double getdata(int i, int j) {
        return data[i][j];
    }
}
