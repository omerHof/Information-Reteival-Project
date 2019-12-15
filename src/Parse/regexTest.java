package Parse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class regexTest {



    public static void main(String[] args) throws IOException {
        String text1 = "POLITICIANS,  PARTY PREFERENCES \n" +
                "\n" +
                "   Summary:  Newspapers in the Former Yugoslav Republic of \n" +
                "   Macedonia have published the results of opinion polls, \n" +
                "   indicating the relative popularity of politicians, \n" +
                "   political parties, and attitudes toward the political system. \n" +
                "\n" +
                "   The 22-23 January edition of the Skopje newspaper VECER in \n" +
                "Macedonian published on pages 6-7 the results of an opinion poll \n" +
                "conducted by the \"BriMa\" agency in November 1993. According to \n" +
                "VECER, 1,036 respondents were classified by age and residence, but \n" +
                "the paper did not explain the methodology or give the margin of \n" +
                "error.  For the purpose of comparison, the paper cited the results \n" +
                "of an unidentified poll made in May 1993. The approval/disapproval \n" +
                "ratings, in percent, for ten Macedonian politicians were: \n" +
                "\n" +
                "                                           November 1993    May 1993 \n" +
                "\n" +
                "Kiro Gligorov, President of the Republic      76/15           78/13 \n" +
                "\n" +
                "Vasil Tupurkovski, former Macedonian          50/36           43/37 \n" +
                "   official in Federal Yugoslavia \n" +
                "\n" +
                "Ljubomir Frckovski, Interior Minister         50/42           42/43 \n" +
                "\n" +
                "Stojan Andov, Parliamentary Chairman          48/41           48/39 \n" +
                "\n" +
                "Branko Crvenkovski, Prime Minister            46/41           44/38 \n" +
                "\n" +
                "Vlado Popovski, Defense Minister              41/41           36/37 \n" +
                "\n" +
                "Stevo Crvenkovski, Foreign Minister           40/43   No Data Given \n" +
                "\n" +
                "Petar Gosev, Democratic Party leader          34/53           40/42 \n" +
                "\n" +
                "Todor Petrov, Independent parliamentarian     32/53   No Data Given \n" +
                "\n" +
                "Nikola Popovski, Social Democratic            29/46           32/42 \n" +
                "   Party parliamentarian \n" +
                "\n" +
                "   VECER noted that President Gligorov's very high approval rating \n" +
                "of 90 percent among those over age 65 fell off to a still high 70 \n" +
                "percent among respondents between 18 and 24.  Residents of Skopje \n" +
                "ranked the politicians in a slightly different order from the \n" +
                "ranking given by the whole sample: Gligorov, Tupurkovski, Frckovski, \n" +
                "Andov, Gosev, Branko Crvenkovski, Vlado Popovski, Petrov, Nikola \n" +
                "Popovski, and Stevo Crvenkovski. \n" +
                "\n" +
                "   The results of a series of opinion polls conducted by the Agency \n" +
                "for Public Opinion Research and published \"exclusively\" by the \n" +
                "Skopje weekly PULS newspaper, confirmed Gligorov's substantial lead \n" +
                "in popularity among political figures.  According to the 31 December \n" +
                "1993 issue of PULS (pages 16-18), the agency gathered the data by \n" +
                "means of telephone interviews with 300 residents in the Republic of \n" +
                "Macedonia during 20-24 December. PULS also provided data from \n" +
                "surveys made in March, June, and September for comparison.  Some of \n" +
                "the following percentages are approximate values that were derived \n" +
                "from the graph published by the paper: \n" +
                "\n" +
                "                         March       June      September    December \n" +
                "\n" +
                "Kiro Gligorov             87          82.33      89.33           89 \n" +
                "Stevo Crvenkovski         54          65         49              63 \n" +
                "Stojan Andov              61          62         60              61 \n" +
                "Branko Crvenkovski        56          60         54 \n" +
                "53.5 \n" +
                "Ljubomir Frckovski        35          45         48              50 \n" +
                "Petar Gosev               50          31         52 \n" +
                "49.53 \n" +
                "Jovan Andonov, \n" +
                " Deputy Prime Minister    39          39         50              37 \n" +
                "Vlado Popovski            18          25         36              35 \n" +
                "Kiro Popovski, Deputy \n" +
                " Chairman, Parliament     26          27         33              32 \n" +
                "Ante Popovski, leader of \n" +
                " MAAK (Movement for All- \n" +
                " Macedonian Action)       29          32         32 \n" +
                "indistinct \n" +
                "Jane Miljovski, Minister \n" +
                " without Portfolio        --          23         31              24 \n" +
                "Vladimir Golubovski \n" +
                " VMRO-DP (Internal \n" +
                " Macedonian Revolutionary \n" +
                " Organization-Democratic \n" +
                " Party) leader            --          30         25              23 \n" +
                "Nevzat Halili \n" +
                " Party for Democratic \n" +
                " Prosperity official      38.33       38         18              18 \n" +
                "\n" +
                "Lj upco Georgievski \n" +
                "VMRO-DPMNE (Internal \n" +
                "Macedonian Revolutionary \n" +
                "Organization-Democratic \n" +
                "Party for Macedonian \n" +
                "National Unity) \n" +
                "official                  18          10         16              17 \n" +
                "Dosta Dimovska \n" +
                "VMRO-DPMNE \n" +
                "official                  --          11         17              16 \n" +
                "\n" +
                "   On pages 6 and 7 of its 15-16 January issue, VECER also published \n" +
                "the results of a November 1993 survey on party preferences. \n" +
                "\"BriMa,\" working with the Gallup organization, interviewed 1,036 \n" +
                "people. \n" +
                "\n" +
                "   Question: \"If elections were held today, for which party would \n" +
                "you vote?\" (all numbers are percentages) \n" +
                "\n" +
                "SDSM (Social Democratic Alliance of Macedonia)  22.8 \n" +
                "VMRO-DPMNE                                      11.2 \n" +
                "Democratic Party (DP, led by Petar Gosev)        6.3 \n" +
                "Socialist Party                                  3.3 \n" +
                "Liberal Party (LP)                               3.2 \n" +
                "Workers Party                                    2.9 \n" +
                "PCERM (Party for the Full Emancipation of \n" +
                "    Romanies in Macedonia)                       1.8 \n" +
                "Democratic Party of Turks in Macedonia           0.8 \n" +
                "MAAK                                             0.3 \n" +
                "Another party                                    4.0 \n" +
                "Undecided                                       18.6 \n" +
                "Would not vote                                   6.6 \n" +
                "\n" +
                "   VECER noted that some parties fared better in certain cities than \n" +
                "their overall scores indicate.  For example, the DP was about twice \n" +
                "as popular in Skopje as elsewhere, getting 12.1 percent in the \n" +
                "capital; the VMRO-DPMNE was more popular in Bitola, getting 15.7 \n" +
                "percent, than in the remainder of the country; and the LP in the \n" +
                "Bregalnica area got the support of 10.6 percent, substantially \n" +
                "higher than the 3.2 percent support it received overall. \n" +
                "\n" +
                "   Question: \"Do you have confidence in the following parties?\" (all \n" +
                "numbers are percentages) \n" +
                "\n" +
                "              Yes           No       Do Not Know \n" +
                "\n" +
                "SDSM           28           51          21 \n" +
                "VMRO-DPMNE     15           72          14 \n" +
                "LP             19           59          22 \n" +
                "PDP-NDP*       20           73           7 \n" +
                "\n" +
                "*Party for Democratic Prosperity-People's Democratic Party \n" +
                "\n" +
                "   The poll clearly indicated that Macedonians have little \n" +
                "confidence in any of the parties currently active in the country. \n" +
                "Respondents were also asked whether it would be good for the country \n" +
                "to have elections sooner than scheduled; 62 percent agreed, 20 \n" +
                "percent disagreed, and 18 percent did not know. These findings were \n" +
                "correlated with party preferences, producing the following results: \n" +
                "Of those who would vote for the SDSM, 54 percent wanted elections \n" +
                "soon, while 34 percent were against early elections. However, 80 \n" +
                "percent of VMRO-DPMNE supporters favored elections soon, as did 79 \n" +
                "percent of LP supporters and 71 percent of DP supporters. While 80 \n" +
                "percent of those surveyed thought that a person should vote (14 \n" +
                "percent did not agree), only 40 percent thought that it was very \n" +
                "important which party won the elections and 27 percent thought it \n" +
                "was somewhat significant. \n";

        String text2 = "Can you imagine the high horses that would be mounted, the shrill outcry,\n" +
                "were BBC2 to promise a programme in which three men discussed what delighted\n" +
                "and disgusted them about women, femininity and female hygiene? But don't\n" +
                "worry, today's 10 x 10 is politically correct to its enamelled fingertips:\n" +
                "it promises three women discussing what delights and disgusts them about\n" +
                "men, masculinity, and male hygiene. So that's OK (10.20). Three Indian\n" +
                "hucksters are the subject of Under The Sun (9.30 BBC2). Shakeel sells kohl\n" +
                "and sings at Muslim festivals; Ansfar peddles home-made potions, he\n" +
                "admitting 'My balm doesn't work on me, I use Vick'; and Hashmat sells a\n" +
                "magic book and is an alcoholic. The subject of Ross McElwee's documentary\n" +
                "for True Stories (9.35 C4) is his own life and family. He says he is 'more\n" +
                "comfortable filming the family than starting one', though the programme\n" +
                "shows him announcing his engagement. If you have managed to avoid hearing\n" +
                "the score in the first day of the Third Test it will take some strength of\n" +
                "character to switch on (11.50 BBC1) and find out.";

        String text3  ="<P>\n" +
                "The Supreme Court ruled Tuesday that the government has wrongly denied monthly \n" +
                "benefits and free medical care to poor disabled children by imposing stringent, \n" +
                "unfair guidelines for eligibility. \n" +
                "</P>\n" +
                "<P>\n" +
                "The ruling could affect tens or even hundreds of thousands of low-income \n" +
                "handicapped children denied Social Security benefits over the last 15 years. \n" +
                "</P>\n" +
                "<P>\n" +
                "The high court ordered federal officials to individually evaluate children to \n" +
                "see if they are disabled rather than rely on a list of disabling conditions, as \n" +
                "they have done since 1974, when children were first made eligible for the \n" +
                "benefits. \n" +
                "</P>\n" +
                "<P>\n" +
                "Although children could qualify for benefits even if their disabilities were \n" +
                "not on the list, they were less likely to receive benefits if the impairments \n" +
                "were not specifically mentioned. \n" +
                "</P>\n" +
                "<P>\n" +
                "The government list includes dozens of afflictions, including cerebral palsy, \n" +
                "epilepsy and acute leukemia, but it does not include such well-known childhood \n" +
                "impairments as spina bifida, Down's syndrome, muscular dystrophy, autism, AIDS \n" +
                "and fetal alcohol syndrome. \n" +
                "</P>\n" +
                "<P>\n" +
                "The class-action suit was filed on behalf of 11-year-old Brian Zebley, who \n" +
                "suffered brain damage at birth. He has eye problems, motor skills difficulties, \n" +
                "is partly paralyzed and retarded. He received benefits for two years, until \n" +
                "surgery enabled him to walk better. But in 1982 his benefits were cut off \n" +
                "because he \"no longer met or equaled the requirements\" set forth in the \n" +
                "regulations. \n" +
                "</P>\n" +
                "<P>\n" +
                "\"This program has been illegally administered for over 15 years,\" said Jonathan \n" +
                "Stein, a Philadelphia lawyer who in 1983 filed the class-action suit that \n" +
                "resulted in Tuesday's ruling. \"This will open up benefits and free medical care \n" +
                "to tens of thousands, and maybe hundreds of thousands, of poor kids who are \n" +
                "disabled.\" \n" +
                "</P>\n" +
                "<P>\n" +
                "The $12-billion-a-year Supplemental Security Income program provides extra \n" +
                "income for poor persons who are over 65, blind or disabled. Beginning in 1974, \n" +
                "Conbgress made poor disabled children eligible for the same benefits as adults \n" +
                "if their mental or physical impairments were of \"comparable severity\" to a \n" +
                "disability that would prevent an adult from \"any substantial gainful activity.\" \n" +
                "</P>\n" +
                "<P>\n" +
                "In response, federal regulators drew up a list of disabling impairments but \n" +
                "ignored a number that are common only among children, the high court noted. In \n" +
                "addition, the regulations made it easier for adults to qualify because, even \n" +
                "without an impairment that was totally disabling, an adult could still show \n" +
                "that he or she could not hold a job. \n" +
                "</P>\n" +
                "<P>\n" +
                "Each year, Social Security officials have rejected about half of the average \n" +
                "100,000 claims filed on behalf of children. \n" +
                "</P>\n" +
                "<P>\n" +
                "Justice Harry A. Blackmun, writing for a 7-2 majority, said that the \n" +
                "government's \"approach to child disability is manifestly contrary to the \n" +
                "statute\" enacted by Congress and is therefore illegal. Only Chief Justice \n" +
                "William H. Rehnquist and Justice Byron R. White sided with government attorneys \n" +
                "in the case (Sullivan, Secretary of Health and Human Services, vs. Zebley, \n" +
                "88-1377). \n" +
                "</P>\n" +
                "<P>\n" +
                "Neither lawyers involved in the case nor Social Security officials could \n" +
                "estimate how much the ruling will cost the government. This year, about 265,000 \n" +
                "children are getting benefits under the program at an annual cost of nearly $1 \n" +
                "billion, Social Security officials said. \n" +
                "</P>\n" +
                "<P>\n" +
                "Studies have estimated that as many as 1 million children nationwide are \n" +
                "impaired and living in low-income families, suggesting a greatly increased \n" +
                "government liability. Social Security officials concede that some severely \n" +
                "disabled children have fallen through the cracks but contend that there has \n" +
                "been no wholesale denial of benefits. \n" +
                "</P>\n" +
                "<P>\n" +
                "The court did not specify whether the government must reopen claims from \n" +
                "previous years. Because the nationwide class-action suit was filed in 1983, \n" +
                "Justice Department lawyers conceded that they would have to reopen claims \n" +
                "denied since then. But Stein, the Philadelphia attorney, said that he would \n" +
                "seek to have claims reopened back to 1974, when the program began. \n" +
                "</P>\n" +
                "<P>\n" +
                "Marilyn Holle, a child advocacy attorney with Protection and Advocacy Inc. in \n" +
                "Glendale, Calif., said that the rigid federal regulations have discouraged many \n" +
                "parents with disabled children from applying for benefits. \n" +
                "</P>\n" +
                "<P>\n" +
                "\"The rules were so restrictive and rigid that a lot of parents decided it was \n" +
                "futile. It was particularly bad for infants and toddlers because their \n" +
                "conditions often did not fit the list,\" Holle said. \n" +
                "</P>\n" +
                "<P>\n" +
                "In California, the program provides a maximum monthly benefit of $499 for a \n" +
                "disabled child. More important, children who qualify become eligible for \n" +
                "Medicaid or MediCal benefits. \n" +
                "</P>\n" +
                "<P>\n" +
                "A couple with a single disabled child may have a monthly income as high as \n" +
                "$1,762 and still qualify for some benefits, Holle said. \n" +
                "</P>\n" +
                "<P>\n" +
                "Parents can apply for benefits at a local office of the Social Security \n" +
                "Administration. \"I also urge parents to talk to a social worker at the \n" +
                "hospital, because they can help with the records,\" Holle said. \n" +
                "</P>\n" +
                "<P>\n" +
                "Meanwhile, in a second ruling designed to protect children, the court said that \n" +
                "parents suspected of abusing a child may be jailed for contempt if they refuse \n" +
                "to obey a court order to disclose the child's whereabouts. \n" +
                "</P>\n" +
                "<P>\n" +
                "The 7-2 ruling neatly disposes of a dilemma raised by a Baltimore mother's case \n" +
                "but did so in an apparently narrow way. \n" +
                "</P>\n" +
                "<P>\n" +
                "Jacqueline Bouknight, a Baltimore woman whose young child, Maurice, had \n" +
                "suffered broken bones and bruises in his first months of life, briefly lost \n" +
                "custody of her child in 1986. But a juvenile court judge gave Bouknight custody \n" +
                "again on the promise that she would improve her behavior and allow regular \n" +
                "visits by child-care workers. \n" +
                "</P>\n" +
                "<P>\n" +
                "She did neither, and in 1987 the child was reported missing by child-care \n" +
                "workers, who suspected he was dead. But, when brought back before the judge, \n" +
                "Bouknight invoked her Fifth Amendment right against self-incrimination and \n" +
                "refused to disclose the whereabouts of Maurice. \n" +
                "</P>\n" +
                "<P>\n" +
                "Justice Sandra Day O'Connor, writing for the court, said that Bouknight may not \n" +
                "invoke the Fifth Amendment because she was under a juvenile court order. This \n" +
                "was primarily a civil regulatory matter, not a criminal one, O'Connor said. \n" +
                "</P>\n" +
                "<P>\n" +
                "Increasingly, the court has adopted this approach to narrow the reach of the \n" +
                "Fourth and Fifth amendments. For example, officials may search persons at \n" +
                "airports or at the U.S. borders without search warrants, the court has said, \n" +
                "because these are primarily regulatory searches, not criminal ones. \n" +
                "</P>\n" +
                "<P>\n" +
                "Similarly, the court has said that corporate officers may not invoke the Fifth \n" +
                "Amendment and refuse to turn over potentially incriminating documents, again \n" +
                "because this is considered a regulatory matter. \n" +
                "</P>\n" +
                "<P>\n" +
                "In 1971, the court upheld a California law requiring motorists involved in \n" +
                "accidents to supply their names and addresses, even if this information could \n" +
                "prove incriminating. \n" +
                "</P>\n" +
                "<P>\n" +
                "Citing these rulings, O'Connor described the juvenile courts as a \"broadly \n" +
                "directed, noncriminal regulatory regime\" designed to protect children, not \n" +
                "prosecute crimes. But O'Connor added that Bouknight, who has remained in a \n" +
                "Baltimore jail, could seek immunity from prosecution for whatever information \n" +
                "she provides. Maurice was last reported seen nearly two years ago. \n" +
                "</P>\n" +
                "<P>\n" +
                "In dissent, Justices Thurgood Marshall and William J. Brennan Jr. said that \n" +
                "forcing a suspect to disclose the whereabouts of a dead body certainly could \n" +
                "prove incriminating and, therefore, should be covered by the Fifth Amendment. \n" +
                "</P>\n" +
                "<P>\n" +
                "Tuesday's ruling in the case (Baltimore vs. Bouknight, 88-1182) has no effect \n" +
                "on the much publicized two-year-long jailing of Dr. Elizabeth Morgan in the \n" +
                "District of Columbia. Morgan hid her daughter rather than allow court-ordered \n" +
                "visitations with the father. Morgan alleged that the father had sexually abused \n" +
                "the child. She was held in jail on civil contempt charges and could not invoke \n" +
                "the Fifth Amendment. \n" +
                "</P>\n" +
                "<P>\n" +
                "In other actions, the court: \n" +
                "</P>\n" +
                "<P>\n" +
                " -- Let stand an appeals court ruling that government social workers are immune \n" +
                "from suit for placing children in foster homes. Three children who were placed \n" +
                "with an uncle in Washington state and were later sexually abused filed suit \n" +
                "against the caseworker. But the U.S. 9th Circuit Court of Appeals based in San \n" +
                "Francisco said these workers, like court employees, are immune from liability \n" +
                "for their official actions if the child-placement decisions are approved by a \n" +
                "court (Babcock vs. Tyler, 89-912). \n" +
                "</P>\n" +
                "<P>\n" +
                " -- Dismissed a damage suit against the federal government filed by the widow \n" +
                "of Navy Capt. Michael J. Smith, one of seven crew members who died in the 1986 \n" +
                "explosion of the Challenger space shuttle. \n" +
                "</P>\n" +
                "<P>\n" +
                "The families of the other six, all civilians, accepted settlements from the \n" +
                "government. But Smith's widow was not made a similar offer because, under a \n" +
                "court doctrine that is still the subject of dispute, civilians may sue the \n" +
                "government for damages but servicemen and their survivors may not. The claim by \n" +
                "Smith's widow, Jane, was rejected by two lower courts, and the justices refused \n" +
                "to reopen the issue (Smith vs. U.S., 89-607). \n" +
                "</P>\n" +
                "<P>\n" +
                "POLICE POWER BOLSTERED -- The high court upheld a ruling that allows police to \n" +
                "stop and question a person. A3 \n" +
                "</P>";
        /*
        for (int i=0;i<100000;i++){
            replacement(text1);
            replacement(text2);
            replacement(text3);

        }

         */
        checkMonths();
        replacement(text1);
        stopWordsStandart("try");







        //System.out.println(text);

        /*
        String num = "450000";
        String numResult = num.replaceAll("[1-9][0-9][0-9][0-9][0-9]|[1-9][0-9][0-9][0-9][0-9]", num.substring(0,num.length()-3)+" K");
        System.out.println(numResult);

        String line = "This order was 70% percent 80 percentage placed for QT3000! OK?";
        String pattern = "(percent)";

        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);

        // Now create matcher object.
        Matcher m = r.matcher(line);
        if (m.find( )) {
            System.out.println("Found value: " + m.group(0) );


        }else {
            System.out.println("NO MATCH");
        }
        */

    }

    private static void replacement(String text){
        //replace percent or percentage to %
        //text = "omer's (is) . 9.3, 9,3 9'6";
        text = "bnB";
        text=text.replaceAll("[^\\d.]", "");
        System.out.println(text);
        text = text.replaceAll(",\\s|,|\\.\\s|\\)|\\(|\\W\\bs\\b", " ");
        System.out.println(text);
        text = text.replaceAll("\\%|\\s\\bpercent\\b|\\s\\bpercentage\\b", "%");

        //replace Thousand to K

        text = text.replaceAll("\\s\\bThousand\\b|\\s\\bthousand\\b","K");
        //System.out.println(text);

        //replace $100 to 100 dollars ----> NOT WORKING!
        //text = text.replaceAll("\\$[\\d]", " dollars");
        //System.out.println(text);
        //\$+\d++\w|

        //replace Million to M

        text = text.replaceAll("\\s\\bMillion\\b|\\s\\bmillion\\b|\\s\\bm\\b\\s","M");
        //System.out.println(text);

        //replace Billion to B
        text = text.replaceAll("\\s\\bBillion\\b|\\s\\bbillion\\b|\\s\\bbn\\b\\s|\\s\\bb\\b\\s","B");
        //System.out.println(text);

        //System.out.println(text);

        //replace U.S. dollars to Dollars
        text = "DSDSDDS U.S. dollars";
        text = text.replaceAll("\\s\\bU.S. dollars\\b| \\s\\bU.S. Dollars\\b "," Dollars");

    }

    /**
     * this test check the time it takes to remove all the stop words in regex strategy
     */
    private  static void stopWordsRegex(String text){

        text = text.replaceAll("\\s\\bU.S. dollars\\b| \\s\\bU.S. Dollars\\b "," Dollars");
    }

    private  static void stopWordsStandart(String doc) throws IOException {
        List stopwords = Files.readAllLines(Paths.get("C:\\Users\\ohoff\\Documents\\information retrieval\\05 stop_words.txt"));
        ArrayList<String> allWords = Stream.of(doc.toLowerCase()
                .split(" "))
                .collect(Collectors.toCollection(ArrayList<String>::new));
        allWords.removeAll(stopwords);
        String result = allWords.stream().collect(Collectors.joining(" "));
        System.out.println(result);
        //assertEquals(result, target);

        ArrayList<Integer> test = new ArrayList<>();
        test.add(1);
        test.add(1);
        test.add(1);
        test.add(1);
        test.add(1);
        ArrayList<ArrayList<Integer>> arrayLists = new ArrayList<>();
        arrayLists.add(test);

    }

    public static void checkMonths (){
        String text = "Ma";
        String month = "([Jj][Aa][Nn](?:[Uu][Aa][Rr][Yy])?|[Ff][Ee][Bb](?:[Rr][Uu][Aa][Rr][Yy])?|[Mm][Aa][Rr](?:[Cc][Hh])?|[Aa][Pp][Rr](?:[Ii][Ll])?|\\bMAY\\b|\\bMay\\b|\\bmay\\b|[Jj][Uu][Nn](?:[Ee])?|[Jj][Uu][Ll](?:[Yy])?|[Aa][Uu][Gg](?:[Uu][Ss][Tt])?|[Ss][Ee][Pp](?:[Tt][Ee][Mm][Bb][Ee][Rr])?|[Oo][Cc][Tt](?:[Oo][Bb][Ee][Rr])?|[Nn][Oo][Vv](?:[Ee][Mm][Bb][Ee][Rr])?|[Dd][Ee][Cc](?:[Ee][Mm][Bb][Ee][Rr])?)";
        Pattern p = Pattern.compile("^"+month+"$");
        Matcher m = p.matcher(text);
        if (m.find()){
            System.out.println("successes");
        }
    }

}
