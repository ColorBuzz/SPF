package com.colorbuzztechgmail.spf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CutNoteGenerator {

    public enum GeneratorType{

        SINGLE,
        MULTI,
        AUTOMATIC

    }

    public static CutNoteList create (Map<String,Integer> sizevalues,float maxPairNumber,PreviewModelInfo model,GeneratorType type) {
        final CutNoteList cutNoteList= new CutNoteList(0);

        GeneratorType mType=type;
        cutNoteList.setModel(model.getName());
        cutNoteList.setReference(14534567+ (int)Math.random());
        cutNoteList.setModelId(model.getId());


        final List<CutNote> cutNotes=new ArrayList<>();
        final Object[] valuesList=sizevalues.values().toArray();
        final List<String> sizeList=new ArrayList<>();

        float maxValue=0;
        float notesCutCount=0;

        for(String key :sizevalues.keySet()){

            sizeList.add(key);


        }

        if(maxPairNumber>0) {

            for (int i = 0; i < valuesList.length; i++) {


                if ((int) valuesList[i] > maxValue) {

                    maxValue = (int) valuesList[i];
                }

            }

            String result = String.valueOf(maxValue / maxPairNumber);




                String[] values1 = new String[2];
                values1 = (result).split("\\.");
//                float divisor= (float) Math.pow(10,values[1].length());
//                float decimal=(Float.valueOf(values[1])/divisor)*maxPairNumber;


                if(Integer.valueOf(values1[1])>0){
                    notesCutCount = (Integer.valueOf(values1[0]) + 1);

                }else {

                    notesCutCount = (Integer.valueOf(values1[0]));

                }



            for (int j = 0; j < notesCutCount; j++) {


                final CutNote cutNote = new CutNote(j);
                cutNote.setDate(Utils.getDate());
                cutNotes.add(cutNote);


            }




            for (String size : sizeList) {

                int pairCount = sizevalues.get(size);


                

                    if ( pairCount == (int) maxPairNumber) {


                        int loop = (int)(sizevalues.get(size) / maxPairNumber);
                        int index = 0;

                        for (int p = 0; p < loop; p++) {

                            if (pairCount > 0) {

                                if (index < cutNotes.size()) {

                                    cutNotes.get(index).addCount(size, cutNotes.get(index).getCountAtSize(size) +(int) maxPairNumber);
                                    pairCount = pairCount - (int) maxPairNumber;
                                    index++;

                                } else {

                                    index = 0;
                                    cutNotes.get(index).addCount(size, cutNotes.get(index).getCountAtSize(size) +(int) maxPairNumber);
                                    pairCount = pairCount - (int) maxPairNumber;


                                }

                            } else {


                                break;
                            }

                        }

                    } else if (pairCount > (int) maxPairNumber) {



                        String[] values = new String[2];
                        values = (String.valueOf(sizevalues.get(size) / maxPairNumber)).split("\\.");
                        // float divisor= (float) Math.pow(10,values[1].length());
                        // float decimal=(Float.valueOf(values[1])/divisor)*maxPairNumber;
                        int loop = (Integer.valueOf(values[0]) + 1);



                    } else if (pairCount < (int) maxPairNumber) {


                        int loop = pairCount;
                        int index = 0;

                        for (int p = 0; p < loop; p++) {

                            if (pairCount > 0) {

                                if (index < cutNotes.size()) {

                                    cutNotes.get(index).addCount(size, cutNotes.get(index).getCountAtSize(size) + 1);
                                    pairCount--;
                                    index++;

                                } else {

                                    index = 0;
                                    cutNotes.get(index).addCount(size, cutNotes.get(index).getCountAtSize(size) + 1);
                                    pairCount--;


                                }

                            } else {


                                break;
                            }

                        }


                    }


            }
        }

        if(cutNotes.size()>0){

            cutNoteList.addNote(cutNotes);
        }
            return cutNoteList;

    }
}
