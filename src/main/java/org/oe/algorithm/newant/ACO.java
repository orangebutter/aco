package org.oe.algorithm.newant;

/**
 * @author 彤
 */
public class ACO {
    private Ant[] ants; //一群蚂蚁
    private int antNum; //蚂蚁数量
    private int cityNum; //城市数量
    private int MAX_GEN; //迭代次数
    private float[][] pheromone; //信息素矩阵
    private double[][] distance; //距离矩阵
    public static double bestLength; //解路径总长度
    public static int[] bestTour; //解路径

    private float alpha;
    private float beta;
    private float rho; //信息素挥发因子
    private int Q; //信息素强度
    private float iQ;//路径信息素初始值


    public ACO() {
    }

    public ACO(int n, int m, int g, float a, float b, float r, int QQ, float initQ) {
        cityNum = n;
        antNum = m;
        ants = new Ant[antNum];
        MAX_GEN = g;
        alpha = a;
        beta = b;
        rho = r;
        Q = QQ;
        iQ = initQ;
    }

    @SuppressWarnings("resource")
    /**
     * 初始化:
     * 从Data类中读入每个城市的坐标信息,根据坐标情况计算距离矩阵->
     * 将信息素矩阵的所有元素初始化为0.1->
     * 将解路径总长度bestLength初始化为正无穷->
     * 按照指定的数目初始化蚁群......
     */
    public void init() {
        double[] x; //存储每个城市的横坐标
        double[] y; //存储每个城市的纵坐标
        System.out.print("从文件中读入每座城市坐标信息............\n");

        distance = new double[cityNum][cityNum];
        x = new double[cityNum];
        y = new double[cityNum];
        //依次的读取文件中的一行,按空格进行分割,对应的付给x与y
        for (int i = 0; i < cityNum; i++) {
            x[i] = Data.getdata(i, 1);
            y[i] = Data.getdata(i, 2);
        }
        System.out.print("正在生成距离矩阵............\n");
        //计算距离矩阵,距离矩阵中的元素是对应两个城市之间的距离
        for (int i = 0; i < cityNum - 1; i++) {
            distance[i][i] = 0;  //对角线为0
            for (int j = i + 1; j < cityNum; j++) {
               /*double rij = Math.sqrt(((x[i] - x[j]) * (x[i] - x[j])+ (y[i] - y[j]) * (y[i] -y[j]))/10.0);
               int tij = (int) Math.round(rij);
               if (tij < rij) {
                    distance[i][j] = tij + 1;
                    distance[j][i] = distance[i][j];
                }else {
                    distance[i][j] = tij;
                    distance[j][i] = distance[i][j];
                 }*/
                double rij = Math.sqrt(((x[i] - x[j]) * (x[i] - x[j]) + (y[i] - y[j]) * (y[i] - y[j])));
                distance[i][j] = rij;
                distance[j][i] = distance[i][j];
            }
        }
        distance[cityNum - 1][cityNum - 1] = 0;

        //初始化信息素矩阵
        pheromone = new float[cityNum][cityNum];
        for (int i = 0; i < cityNum; i++) {
            for (int j = 0; j < cityNum; j++) {
                pheromone[i][j] = iQ;  //初始化为0.1
            }
        }
        bestLength = Integer.MAX_VALUE;
        bestTour = new int[cityNum + 1];
        //初始化指定数目的蚂蚁
        System.out.print("一大拨蚂蚁正在孵化......" + antNum + "只......\n");
        for (int i = 0; i < antNum; i++) {
            ants[i] = new Ant(cityNum);
            ants[i].init(distance, alpha, beta);
        }
    }

    /**
     * 核心求解过程
     * 蚂蚁逐个进行遍历->
     * 每只蚂蚁遍历过一次后更新信息素矩阵->
     * 重复上一步骤直至MAX_GEN次->
     * 得到最优解,打印输出..............
     */
    public void solve() {
        double dis;
        for (int g = 0; g < MAX_GEN; g++) {
            for (int i = 0; i < antNum; i++) {
                for (int j = 1; j < cityNum; j++) {
                    ants[i].selectNextCity(pheromone);//每只蚂蚁,根据信息素矩阵,完成对所有城市的遍历
                }
                ants[i].getTabu().add(ants[i].getFirstCity()); //蚂蚁最终回到出发的城市
                if (ants[i].getTourLength() < bestLength) { //用找到最优路径的蚂蚁设置解路径总长度
                    bestLength = ants[i].getTourLength();
                    for (int k = 0; k < cityNum + 1; k++) { //找到最优路径的蚂蚁,根据它禁忌表中节点添加的顺序,设置解路径
                        bestTour[k] = ants[i].getTabu().get(k).intValue();
                    }
                }

                if (NewJFrame.flag == 1) {  //传统模型
                    System.out.print("传统模型............");
                    for (int j = 0; j < cityNum; j++) { //一只蚂蚁为它经历过的每段路程增添的信息素
                        ants[i].getDelta()[ants[i].getTabu().get(j).intValue()][ants[i].getTabu().get(j + 1).intValue()] = (float)
                                ((1. / ants[i].getTourLength()) * Q);

                        ants[i].getDelta()[ants[i].getTabu().get(j + 1).intValue()][ants[i].getTabu().get(j).intValue()] = (float)
                                ((1. / ants[i].getTourLength()) * Q);
                    }
                } else if (NewJFrame.flag == 0) { //蚁恒模型
                    System.out.print("蚁恒模型............");
                    for (int j = 0; j < cityNum; j++) { //一只蚂蚁,为它经历过的每段路程增添的信息素
                        dis = distance[ants[i].getTabu().get(j).intValue()][ants[i].getTabu().get(j + 1).intValue()];
                        ants[i].getDelta()[ants[i].getTabu().get(j).intValue()][ants[i].getTabu().get(j + 1).intValue()] = (float)
                                ((dis * (1. / ants[i].getTourLength())) * Q);

                        ants[i].getDelta()[ants[i].getTabu().get(j + 1).intValue()][ants[i].getTabu().get(j).intValue()] = (float)
                                ((dis * (1. / ants[i].getTourLength())) * Q);
                    }
                }
            }

            //每只蚂蚁完成了一次遍历
            //System.out.print("每只蚂蚁遍历过一次这是第"+(g+1)+"次............");
            //更新信息素矩阵
            updatePheromone();

            //重新初始化蚂蚁
            for (int i = 0; i < antNum; i++) {
                ants[i].init(distance, alpha, beta);
            }
        }
        System.out.print("完成了 " + MAX_GEN + " 次迭代............");
        //打印最佳结果
        printOptimal();
    }

    //更新信息素矩阵
    private void updatePheromone() {
        //信息素挥发
        NewJFrame.updateTimes++;
        for (int i = 0; i < cityNum; i++)
            for (int j = 0; j < cityNum; j++) {
                pheromone[i][j] = pheromone[i][j] * (1 - rho) + 0.0001f; //rho是信息素挥发因子
            }
        //信息素添加
        for (int i = 0; i < cityNum; i++) {
            for (int j = 0; j < cityNum; j++) {
                for (int k = 0; k < antNum; k++) {
                    //经过某段路程的所有蚂蚁留下的信息素增量+固有量
                    pheromone[i][j] += ants[k].getDelta()[i][j];
                }
            }
        }
        for (int i = 0; i < cityNum; i++) {
            for (int j = 0; j < cityNum; j++) {
                System.out.print(pheromone[i][j] + "\t");
            }
            System.out.print("\n");
        }
        System.out.print("update ok~::::" + NewJFrame.updateTimes + "\n");
    }

    private void printOptimal() {
        System.out.println("解路径的总长度   " + bestLength);
        System.out.println("解路径的节点顺序  ");
        for (int i = 0; i < cityNum + 1; i++) {
            System.out.print(bestTour[i] + "--->");
            if (i % 24 == 0 && i != 0) {
                System.out.print("\n\n\n");
            }
        }
    }

    public Ant[] getAnts() { //返回一群蚂蚁
        return ants;
    }

    public void setAnts(Ant[] ants) { //设置这群蚂蚁
        this.ants = ants;
    }

    public int getAntNum() { //返回蚂蚁数量
        return antNum;
    }

    public void setAntNum(int m) { //设置蚂蚁数量
        this.antNum = m;
    }

    public int getCityNum() { //返回城市数目
        return cityNum;
    }

    public void setCityNum(int cityNum) { //设置城市数目
        this.cityNum = cityNum;
    }


    public int getMAX_GEN() { //返回迭代次数
        return MAX_GEN;
    }

    public void setMAX_GEN(int mAX_GEN) { //设置迭代次数
        MAX_GEN = mAX_GEN;
    }

    public float[][] getPheromone() { //返回信息素矩阵
        return pheromone;
    }

    public void setPheromone(float[][] pheromone) { //设置信息素矩阵
        this.pheromone = pheromone;
    }

    public double[][] getDistance() { //返回距离矩阵
        return distance;
    }

    public void setDistance(double[][] distance) { //设置距离矩阵
        this.distance = distance;
    }

    public double getBestLength() { //返回解路径总长度
        return bestLength;
    }

    public void setBestLength(int bestLength) { //设置解路径总长度
        this.bestLength = bestLength;
    }

    public int[] getBestTour() { //返回解路径
        return bestTour;
    }

    public void setBestTour(int[] bestTour) { //设置解路径
        this.bestTour = bestTour;
    }

    public float getAlpha() { //返回参数阿尔法
        return alpha;
    }

    public void setAlpha(float alpha) { //设置参数阿尔法
        this.alpha = alpha;
    }


    public float getBeta() { //返回参数β
        return beta;
    }

    public void setBeta(float beta) { //设置参数β
        this.beta = beta;
    }

    public float getRho() { //返回信息素挥发因子
        return rho;
    }

    public void setRho(float rho) { //设置信息素挥发因子
        this.rho = rho;
    }

   /*public static void main(String[] args) throws IOException {
     System.out.println("~~~~~~~~蚁 群 算 法 求 解 旅 行 商 问 题
~~~~~~~~~~~~~~~~~~~~~~~~\n" );
     ACO aco = new ACO(parameter.CityNum, parameter.AntNum, parameter.Gen_Num,
parameter.alf, parameter.bta, parameter.Rho);
     aco.init();
     aco.solve();
   }*/
}
