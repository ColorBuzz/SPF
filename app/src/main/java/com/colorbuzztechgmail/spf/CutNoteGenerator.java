package com.colorbuzztechgmail.spf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CutNoteGenerator {

    public enum GeneratorType{

        SINGLE,
        MULTI,
        AUTOMATIC

    }


    public static CutNoteList create (Map<String,Integer> sizevalues,float maxPairNumber,PreviewModelInfo model,GeneratorType type,boolean mix) {

        final CutNoteList cutNoteList= new CutNoteList(0);

        GeneratorType mType=type;
        cutNoteList.setModel(model.getName());
        cutNoteList.setModelId(model.getId());
        int noteCount=1;



        switch (type){

            case AUTOMATIC:

                if(mix){
                    cutNoteList.addCutNote(AutomaticMixedCutNote(sizevalues,maxPairNumber,model.getName()));

                }else {

                    cutNoteList.addCutNote(AutomaticCutNote(sizevalues,maxPairNumber,model.getName()));

                }



              break;

            case SINGLE:
                cutNoteList.addCutNote(createSingleCutNote(sizevalues,model.getName()) );


              break;

            case MULTI:
                cutNoteList.addCutNote(createSingleCutNote(sizevalues,model.getName()) );

                break;


        }

        int pairCount=0;

        for(CutNote cutNote:cutNoteList.getCutNoteList()){

            pairCount=pairCount+cutNote.getPairCount();

        }
        cutNoteList.setMaxPairCount((int)maxPairNumber);
        cutNoteList.setTotalPairCount(pairCount);
        cutNoteList.setNoteCount(cutNoteList.getCutNoteList().size());



            return cutNoteList;

    }



    private  static List<CutNote> AutomaticCutNote(Map<String,Integer> sizevalues,float maxPairNumber,String model){





        final List<CutNote> cutNotes=new ArrayList<>();
        final CutNoteList auxCutNotes=new CutNoteList(0);


        final List<String> sizeList=new ArrayList<>();

        Utils.IdMaker mIdMaker= new Utils.IdMaker();
        mIdMaker.reset();



        for (String size : sizevalues.keySet()){

            sizeList.add(size);

        }

        Collections.sort(sizeList);

            for (String size : sizeList) {

            int pairCount = sizevalues.get(size);

                if (pairCount > 0) {

                    if (pairCount == (int) maxPairNumber) {


                        cutNotes.add(createCutNote(sizeList,model,mIdMaker.next(),0,size,(int) maxPairNumber));
                        sizevalues.put(size,0);
                    } else if (pairCount > (int) maxPairNumber) {

                        String[] values = new String[2];
                        values = (String.valueOf(pairCount / maxPairNumber)).split("\\.");
                        // float divisor= (float) Math.pow(10,values[1].length());
                        // float decimal=(Float.valueOf(values[1])/divisor)*maxPairNumber;
                        int loop = (Integer.valueOf(values[0]) + 1);

                        for (int p = 0; p < loop; p++) {

                            if (pairCount >= maxPairNumber) {
                                cutNotes.add(createCutNote(sizeList,model,mIdMaker.next(),0,size,(int) maxPairNumber));

                                pairCount = pairCount - (int) maxPairNumber;
                                sizevalues.put(size,pairCount);

                            }


                        }


                    }


                }

            }





        for (int i=0;i<sizeList.size();i++) {

            String size=sizeList.get(i);

            int pairCount = sizevalues.get(size);

            if (pairCount > 0) {

                if(auxCutNotes.getCutNoteList().size()==0){
                    auxCutNotes.addCutNote(createCutNote(sizeList,model,mIdMaker.next(),0,null,0));
                }


                         for(CutNote cutNote:auxCutNotes.getCutNoteList()){

                             if(cutNote.getPairCount()<maxPairNumber) {

                                 if ((cutNote.getPairCount() + pairCount) <= maxPairNumber) {

                                     cutNote.addCount(size, (cutNote.getCountAtSize(size) + pairCount));
                                     sizevalues.put(size, 0);


                                 } else {

                                     int total = cutNote.getPairCount();
                                     int loop = Math.abs((int) maxPairNumber - total);

                                     do {

                                         cutNote.addCount(size, cutNote.getCountAtSize(size) + 1);

                                         sizevalues.put(size, sizevalues.get(size)-1);
                                         loop--;


                                     } while (loop > 0);

                                 }

                             }
                         }

                         if(sizevalues.get(size)>0){

                             auxCutNotes.addCutNote(createCutNote(sizeList,model,mIdMaker.next(),0,null,0));
                             i--;


                         }


                    }


                }

                if(auxCutNotes.getCutNoteList().size()>0){

                 cutNotes.addAll(auxCutNotes.getCutNoteList());
                }







        return cutNotes;


    }

    private  static List<CutNote> AutomaticMixedCutNote(Map<String,Integer> sizevalues,float maxPairNumber,String model){





        final List<CutNote> cutNotes=new ArrayList<>();
        final CutNoteList auxCutNotes=new CutNoteList(0);
        int totalPairCount=0;

        final List<String> sizeList=new ArrayList<>();

        Utils.IdMaker mIdMaker= new Utils.IdMaker();
        mIdMaker.reset();


        for (String size : sizevalues.keySet()){

            sizeList.add(size);
            totalPairCount=sizevalues.get(size)+totalPairCount;

        }

        int maxCutnotes;
        String[] values = new String[2];
        values = (String.valueOf(totalPairCount / maxPairNumber)).split("\\.");
        if(Integer.valueOf(values[1])>0){
            maxCutnotes= (Integer.valueOf(values[0]) + 1);

        }else{
            maxCutnotes= (Integer.valueOf(values[0]));

        }

        for(int j=0;j<maxCutnotes;j++){

            cutNotes.add(createCutNote(sizeList,model,mIdMaker.next(),0,null,0));


        }


        Collections.sort(sizeList);

      int count=0;

        for (int i = 0; i < maxCutnotes; i++) {

             for (int j=0;j<sizeList.size();j++) {

               int pairCount = sizevalues.get(sizeList.get(j));

                 if(pairCount>0) {

                 if(cutNotes.get(i).getPairCount()<maxPairNumber) {


                          pairCount = pairCount - 1;
                          cutNotes.get(i).addCount(sizeList.get(j), cutNotes.get(i).getCountAtSize(sizeList.get(j)) + 1);
                          sizevalues.put(sizeList.get(j), pairCount);
                          count++;
                        }

                 }


             }
            if(cutNotes.get(i).getPairCount()<maxPairNumber) {

                if(count<totalPairCount){
                    i--;

                }else{

                    break;
                }

            }


            if(i==maxCutnotes-1){
                if(count<totalPairCount){
                    i=-1;

                }else{

                    break;
                }
            }


        }







        if(auxCutNotes.getCutNoteList().size()>0){

            cutNotes.addAll(auxCutNotes.getCutNoteList());
        }







        return cutNotes;


    }


    private static CutNote createCutNote(List<String> sizeValues,String model,int noteNumbre,int reference,String size,int pairCount) {



        final CutNote cutNote = new CutNote(noteNumbre);
        cutNote.setModel(model);
        cutNote.setReference(reference);
        cutNote.setDate(Utils.getDate());
        cutNote.addSizeList(sizeValues);
        cutNote.addCount(size, (int) pairCount);



        return cutNote;
    }

     public static CutNote createSingleCutNote(Map<String,Integer> sizeValues,String model){

        CutNote cutNote=new CutNote(1);
        cutNote.setModel(model);
        cutNote.setDate(Utils.getDate());


        for(String key :sizeValues.keySet()){

           cutNote.addCount(key, sizeValues.get(key));


        }


        return cutNote;
    }

}
