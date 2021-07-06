package rs.ac.uns.ftn.devops.tim5.nistagrampost.constants;

import java.util.ArrayList;
import java.util.Arrays;

public class TagConstants {

    private TagConstants(){}

    public static ArrayList<String> VALID_TAG_NAMES = new ArrayList<>(
                                                                Arrays.asList("tag1",
                                                                              "tag2",
                                                                              "tag3"));

    public static int  NUMBER_OF_TAGS = 3;

}
