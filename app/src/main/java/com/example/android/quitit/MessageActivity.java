package com.example.android.quitit;

/**
 * Created by Pulkit on 06-08-2017.
 */

class MessageActivity {

    //public static String getMessage(int age, String sex, boolean isChewing, int chew_freq, boolean isSmoking, int smoke_freq, String med_history, String m_status, String habbit_reason, String quit_status, String craving_time, String morning_status) {
    public static String getMessage(Entry Patient, boolean isChewing, boolean isSmoking){
        String message = "";
        String ageCategory=getAgeCategory(Patient.getAge());
//        ="Tobacco is something that affects all your body parts from head to toe and It's consumption is deadly in any form. Hence, There are a few factors we'd like you to consider.";

        if(( isSmoking&&Patient.getSmoke_freq()<3 )||(isChewing && Patient.getChew_freq()<3)) {
            message+=" You are not addicted, yet, Congratulations. But, still, there are a few factors we'd like you to consider.";
        }

        else {
            if (Patient.getSmoke_freq() >= 3 && isSmoking) {
                if (isChewing)
                    message += " According to your data, We have concluded that you are addicted to Smoking.";
            }

            if (Patient.getChew_freq() >= 3 && isChewing) {
                if(isSmoking)
                    message += "And, also chewable forms of tobacco.";
                else
                    message += " According to your data, We have concluded that you are addicted to chewable forms of tobbacco.";
            }
        }

        if(isSmoking){
            message+=" Smoking causes lung diseases and accumulation of tar in lungs.";
        }
        if(isChewing){
            message+=" Gutkha or Khaini, also cause loose gums and mouth problems. When you spit it, it increases risk of spread of TB.";
        }

        if(ageCategory.equals("Youngster") || ageCategory.equals("Middle")) {
            message += " It may affect your face: In the form of dullness of skin as well as wrinkles too early for your age,";

            if(isSmoking)
                message+=" you will suffer drastic drops in your stamina, as well as,";

            message+=" you will your ability to enjoy good food.";

            message+=" Your smile and looks will also be affected as it causes stains on teeth and gums."+
                    " It causes bad odour making you less presentable in public talking.";
            if(ageCategory.equals("Youngster")){
                message+=" Especially, in interviews for jobs.";
            }
            if(Patient.getMarry_status().equals("Married") || Patient.getMarry_status().equals("Soon to be married")) {
                if(Patient.getSex().equals("Female"))
                    message += " Studies have also shown that females who consume have troubles in conceiving new their youngones. It has shown cause miscarriage and infertility in women.";
                else
                    message +=" Contrary to the way it has been marketed, Studies have shown that it causes sexual impotence in men.";
            }
        }

        else if(ageCategory.equals ("Old")) {
            message += " It may shorten your life by upto 10-15 years.";
            if(Patient.getSex().equals("Male"))
                message +=" Contrary to the way it has been marketed, Studies have shown that it causes sexual impotence in men.";
        }

        if(Patient.getMarry_status().equals("Married") && isSmoking)
            message+=" Your smoking brings diseases not just for yourself, but also for your family, especially children and" +
                    " others around you. Even if you avoid smoking at home, kids in your house are still at risk of asthama and allergies"+
                    " because its particle adhere to your clothes";


        if(Patient.getHabit_reason().contains("With Friends")){
            message+=" You may have started these habbits casually with your friends but It becomes an addction really quickly.";
        }

        if((ageCategory=="Middle" || ageCategory=="Old")) {
            message += " Smoking, generally, increases your risk of diabetes and its complication.";
            if (Patient.getMed_history().equals(R.string.disease_1)) {
                message += " Since, you already have it, it may increase its complications by upto 30-40%.";
            }
            if ((Patient.getMed_history().contains(String.valueOf(R.string.disease_2)) || Patient.getMed_history().equals(String.valueOf(R.string.disease_3)))) {
                message += " It raises your blood pressure and heart rate, narrows your arteries, hardens their walls, and makes your blood more likely to clot."+
                        " It stresses your heart and sets you up for a heart attack or stroke.";
            }
        }


        if(Patient.getMorning_status().equals("Yes")) {
            message+=" Your risk of developing lung ,head or neck cancers is considerably higher than that of people who do not consume any tobacco by nearly 50%.";
        }

        if(Patient.getMed_history().contains(String.valueOf(R.string.disease_2)) || Patient.getMed_history().contains(String.valueOf(R.string.disease_3))){
            message+=" If you leave smoking, with in 20 minutes Your blood pressure and pulse rate is back to normal. Your chances of heart attack start going down with in 8 hrs if you quit smoking and in 1 year it drops to 50%";
        }
//        message+=" So, You must be able to resist the thoughts and the cravings. It's best that you think of something that you like or listen to music or talk to someone you like during that time." +
//                " Studies have shown that they are extremely temporary and all one needs to do is divert their mind from it.";
//        message +=" We hope that we were able to motivate and change your perception about tobacco and its harms. We wish you to healthy and tobacco free life ahead." +
//                " Let's QuitIt.";

        return message;
    }

    private static String getAgeCategory(int age) {
        String category="";
        if(age<=24) {
            category="Youngster";
        }
        else if(age>24 && age<=40) {
            category="Middle";
        }
        else{
            category="Old";
        }
        return category;
    }
}
