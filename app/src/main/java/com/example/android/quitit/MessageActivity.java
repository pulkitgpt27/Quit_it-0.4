package com.example.android.quitit;

/**
 * Created by Pulkit on 06-08-2017.
 */

class MessageActivity {

    public static String getMessage(int age, String sex,boolean isChewing, int chew_freq,boolean isSmoking, int smoke_freq,String med_history,String m_status,String habbit_reason) {
        String message="Tobacco affect all your body parts from head to toe";
        String ageCategory=getAgeCategory(age);
        if(ageCategory=="Middle" || ageCategory=="Old")
        {
            message+="\nIt shortens your life by 10 to 15 years\nYou are more prone to stomach ulcers";
        }

        if(ageCategory=="Youngster" && smoke_freq<5 && isSmoking)
        {
            message+="\nIts ill effects are there even if you smoke occasionaly";
        }

        if(habbit_reason=="With Friends" && ageCategory=="Youngster")
        {
            message+="\nYou may have started tobacco use casually ith friends but it soon becomes addiction";
        }

        if(m_status=="Married")
        {
            message+="\nYour smoking brings diseases not just for yourself, but also for your family, especially children and\n" +
                    "others around you\nEven if you avoid smoking at home your kids are still at risk of asthama,allergies as its particle adhere\n" +
                    "to your clothes\n";
        }

        if(isChewing)
        {
            message+="\nCigars, beedis and smokeless tobacco (panmasala, gutkha, khaini) are not any safer than cigarettes.\n" +
                    "They contain nicotine and cancer causing chemicals just like cigarettes\nChewable tobacco causes loose gums and mouth problems\nWhen you spit gutkha ,Khaini,tobacco it increases risk of spread of TB\nSmokeless forms of tobacco are not less dangerous";
        }

        if(ageCategory=="Younster" && sex=="F")
        {
            message+="\nYour smile and looks are affected as it causes stains on teeth and gums\nSmoking and gutkha causes bad odour making you less presentable in public talking and at work.";
        }

        if(ageCategory=="Youngster")
        {
            message+="\nYour ability to enjoy sports is affected\nYour ability to taste good food is affected\nIt reduces your stamina";
        }

        if(isSmoking)
        {
            message+="\nHookah is not safe infact its more harmful then bidi or cigratte";
        }

        if(m_status== "Married" && sex=="M")
        {
            message+="\nContrary to the way it has been marketed it causes Sexual impotence in man";
        }

        if(m_status== "Married" && sex=="F")
        {
            message+="\nTobacco is a cause of miscarriage and infertility in woman.";
        }

        if((ageCategory=="Middle" ||ageCategory=="Old") && med_history!="")
        {
            message+="\nTobacco use causes lung cancer\nIt’s a cause for breathing problems";
        }

        if(med_history.equals(R.string.disease_1) && (ageCategory=="Middle" || ageCategory=="Old"))
        {
            message+="\nSmoking increases your risk of diabetes and its complication(30-40%)";
        }

        if(med_history.equals(R.string.disease_2) && (ageCategory=="Middle" || ageCategory=="Old"))
        {
            message+="\nSmoking and chewing tobacco increases your risk of raised BP/Hypertension";
        }

        if(med_history.equals(R.string.disease_3) && (ageCategory=="Middle" || ageCategory=="Old"))
        {
            message+="\nTobacco increases your chances of heart attack";
        }

        if(med_history.equals(R.string.disease_4) && (ageCategory=="Middle" || ageCategory=="Old"))
        {
            message+="\nIt increases your blood cholesterol levels.";
        }

        if(med_history!="" && ageCategory=="Youngster")
        {
            message+="\nTobacco makes you prone to High blood pressure, high chances of a heart attack and stroke, smoker’s\n" +
                    "cough and lung disease, diabetes and high levels of cholesterol";
        }

        if(age<=35 && sex=="F")
        {
         message+="\nBabies born to mothers who smoke are sicker and smoking causes 4000 new babies to die each\n" +
                 "year in some countries.";
        }

        if(med_history.equals(R.string.disease_1) || med_history.equals(R.string.disease_2) || med_history.equals(R.string.disease_3))
        {
            message+="\nIf u leave smoking with in 20 minutes Your blood pressure and pulse rate is back to normal\nYour chances of heart attack start going down with in 8 hrs if you quit smoking and in 1 year it\n" +
                    "drops to 50%";
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
