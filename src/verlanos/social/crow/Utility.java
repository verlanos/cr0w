package verlanos.social.crow;

/**
 * Created by Sefverl on 8/3/14.
 */
public class Utility {
    public static String pluraliseAndPad(int quant,String str){
        return (quant>1) ? " "+str+"s " : " "+str+" ";
    }
}
