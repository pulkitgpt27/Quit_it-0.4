package com.example.android.quitit;

/**
 * Created by Pulkit on 06-08-2017.
 */

class MessageActivity {

    public static String getMessage(int age, String sex, boolean isChewing, int chew_freq, boolean isSmoking, int smoke_freq, String med_history, String m_status, String habbit_reason, String quit_status, String craving_time, String morning_status) {
        String message="Tobacco affect all your body parts from head to toe general";
        String ageCategory=getAgeCategory(age);

        if(quit_status == "Yes")
        {
            message+="You are at the right place.We assure we will make you quit";
        }

        if(smoke_freq<3 && isSmoking)
        {
            message+="\nCongrats you are not addicted yet we can help you quit tobacco habbits";
        }

        if(smoke_freq>=3 && isSmoking)
        {
            message+="\nAre you addicted dont worry we will help you quit smoking";
        }


        if(chew_freq<3 && isChewing)
        {
            message+="\nCongrats you are not addicted yet we can help you quit tobacco habbits";
        }

        if(chew_freq>=3 && isChewing)
        {
            message+="\nAre you addicted dont worry we will help you quit smoking";
        }

        if(ageCategory.equals("Youngster"))
        {
            message += "\nIt may affect your face and also your stamina also ability to enjoy good food is affected";
        }
        if(ageCategory.equals("Middle"))
        {
            message += "\nIt may affect your family";
        }
        if(ageCategory.equals ("Old"))
        {
            message += "\nIt may shorten your life and of people around you\n";
        }

        if(habbit_reason.contains("With Friends"))
        {
            message+="\nYou may have started tobacco use casually with friends but it soon becomes addiction";
        }

        if(m_status.equals("Married"))
        {
            message+="\nYour smoking brings diseases not just for yourself, but also for your family, especially children and\n" +
                    "others around you.Even if you avoid smoking at home your kids are still at risk of asthama,allergies as its particle adhere\n" +
                    "to your clothes\n";
        }
        if(isChewing)
        {
            message+="\nChewable tobacco causes loose gums and mouth problems\nWhen you spit gutkha ,Khaini it increases risk of spread of TB\n";
        }

        if(ageCategory.equals("Younster") && sex.equals("Female"))
        {
            message+="\nYour smile and looks are affected as it causes stains on teeth and gums\nSmoking and gutkha causes bad odour making you less presentable in public talking and at work.";
        }

        if(isSmoking)
        {
            message+="\nSmoking is not safe it cause lung diseases and accumulation of tar in lungs";
        }

        if((m_status.equals("Married") || m_status.equals("Soon to be married")) && sex.equals("Male"))
        {
            message+="\nContrary to the way it has been marketed it causes Sexual impotence in man";
        }


        if((m_status.equals("Married") || m_status.equals("Soon to be married")) && sex.equals("Female"))
        {
            message+="\nTobacco is a cause of miscarriage and infertility in woman.";
        }
        if(med_history.equals(R.string.disease_1) && (ageCategory=="Middle" || ageCategory=="Old"))
        {
            message+="\nSmoking increases your risk of diabetes and its complication(30-40%)";
        }

        if((med_history.contains(String.valueOf(R.string.disease_2)) || med_history.equals(String.valueOf(R.string.disease_3)))  && (ageCategory.equals("Middle") || ageCategory.equals("Old")))
        {
            message+="\n It raises your blood pressure and heart rate, narrows your arteries and hardens their walls, and makes your blood more likely to clot\nIt stresses your heart and sets you up for a heart attack or stroke.";
        }

        if(!craving_time.equals(""))
        {
            message+="\nIt's best that you think of something that you like or listen to music or keep talking to someone you love during your craving time";
        }

        if(morning_status.equals("Yes"))
        {
            message+="Your risk of developing lung and head and neck cancers is considerably higher than that of non-smokers";
        }

        if(med_history.contains(String.valueOf(R.string.disease_2)) || med_history.contains(String.valueOf(R.string.disease_3)))
        {
            message+="\nIf u leave smoking with in 20 minutes Your blood pressure and pulse rate is back to normal\nYour chances of heart attack start going down with in 8 hrs if you quit smoking and in 1 year it drops to 50%";
        }

        return message;
    }

    private static String getAgeCategory(int age) {
        String category="";
        if(age<=20)
        {
            category="Youngster";
        }
        else if(age>20 && age<=40)
        {
            category="Middle";
        }
        else
            {
                category="Old";
            }
            return category;
    }
}
