public class run {
    public static void main(String[] args) {
        System.out.println("experiment1:");
        System.out.println("OneTestSuite");
        OneTestSuite.run();
        modifyExp.run();
        exp.run();
        PLEDGEexp.run();

        System.out.println("experiment2:");
        Adaptive.run();
    }
}
