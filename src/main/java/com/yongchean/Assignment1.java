package com.yongchean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.*;


public class Assignment1 {
    //global variable
    static ArrayList<Student> student_list = new ArrayList();


    public static void main(String []arg) throws IOException {
        Github();
        Result();
    }

    //Get all the github information from website
    private static void Github() throws IOException {
        //get class students html code
        String url_students = "https://github.com/STIW3054-A201/Main-Data/wiki/List_of_Student";
        Document document_students = Jsoup.connect(url_students).get();
        Element body_students = document_students.body();
        Elements students = body_students.getElementsByTag("tr");

        //get github account html code
        String url_issue1 = "https://github.com/STIW3054-A201/Main-Data/issues/1";
        Document document_issue1 = Jsoup.connect(url_issue1).get();
        Element body_issue1 = document_issue1.body();
        Elements issues1 = body_issue1.getElementsByClass("d-block comment-body markdown-body  js-comment-body");

        //get comment number from issue 4 html code
        String url_issue4 = "https://github.com/STIW3054-A201/Main-Data/issues/4";
        Document document_issue4 = Jsoup.connect(url_issue4).get();
        Element body_issue4 = document_issue4.body();
        Elements issues4 = body_issue4.getElementsByClass("author link-gray-dark css-truncate-target width-fit");

        //get every student information, except github link
        int j = 0;
        for (Element student : students) {
            if (j != 0) {
                Elements student_no = student.select("td:eq(0)");
                Elements student_matric = student.select("td:eq(1)");
                Elements student_name = student.select("td:eq(2)");

                student_list.add(new Student(student_no.text(), student_matric.text(), student_name.text(), "", "", 0));
                //System.out.println(student_list.get(j - 1).get_number() + " " + student_list.get(j - 1).get_matric() + " " + student_list.get(j - 1).get_name());
            }
            j++;
        }

        //set link to student from issue1
        int i = 0;
        for (Element issue1 : issues1) {
            if (i != 0) {
                Elements issue1_comment = issue1.getElementsByTag("p");
                //System.out.println(issue1_comment.text());
                for (Student student : student_list){
                    if (issue1_comment.text().contains(student.get_matric())){
                        String link = issue1_comment.select("a").text();
                        String github_id = link.replaceAll("https://github.com/", "");
                        student.set_link(link);
                        student.set_github_id(github_id);

                        //System.out.println(student.get_link());
                    }
                }
            }
            i++;
        }

        //set comment number to student from issue 4
        int k = 0;
        for (Element issue4 : issues4) {
            if (k != 0) {
                Elements issue4_comment = issue4.getElementsByTag("a");
                for (Student student : student_list){
                    if (issue4_comment.text().contains(student.get_github_id()) && !student.get_github_id().equals("")){
                        //check for arrangement of submission issue 4
                        //System.out.println(student.get_name());
                        student.set_comment_number(student.get_comment_number()+1);
                    }
                }
            }
            k++;
        }
    }

    //Print all the github information to console and excel
    private static void Result() {
        try {
            Workbook xlsx_workbook = new XSSFWorkbook();
            xlsx_workbook.createSheet("github");
            Sheet get_sheet = xlsx_workbook.getSheetAt(0);
            String[] header1 = {"No.", "Matric", "Name", "GitHub Link"};
            String[] header2 =  {"No.", "Matric", "Name", "Comment Numbers"};

            //Font header_font = xlsx_workbook.createFont();
            //header_font.setBold(true);

            //91 underline
            //Print out and insert excel for students who have submitted the GitHub account
            System.out.println(" Issue 1 Submission");
            System.out.println(" * Students who have submitted the GitHub account");
            System.out.println("____________________________________________________________________________________________");
            System.out.printf("| %-3s | %-6s | %-35s | %-35s |\n", "No.", "Matric", "Name", "GitHub Link");
            System.out.println("|_____|________|_____________________________________|_____________________________________|");
            //int counter_for_submitted = 1;
            int sheet1_row_counter = 0;
            int i = 0;
            for (Student student : student_list) {
                if (!student.get_link().equals("")) {
                    i++;
                    System.out.printf("| %-3s | %-6s | %-35s | %-35s |\n", i, student.get_matric(), student.get_name(), student.get_link());
                    Row row = get_sheet.createRow(sheet1_row_counter);
                    if (sheet1_row_counter == 0) {
                        Cell cell1 = row.createCell(0);
                        Cell cell2 = row.createCell(1);
                        Cell cell3 = row.createCell(2);
                        Cell cell4 = row.createCell(3);
                        cell1.setCellValue(header1[0]);
                        cell2.setCellValue(header1[1]);
                        cell3.setCellValue(header1[2]);
                        cell4.setCellValue(header1[3]);
                        sheet1_row_counter++;
                        row = get_sheet.createRow(sheet1_row_counter);
                    }
                    Cell cell1 = row.createCell(0);
                    Cell cell2 = row.createCell(1);
                    Cell cell3 = row.createCell(2);
                    Cell cell4 = row.createCell(3);
                    cell1.setCellValue(i);
                    cell2.setCellValue(student.get_matric());
                    cell3.setCellValue(student.get_name());
                    cell4.setCellValue(student.get_link());
                    sheet1_row_counter++;
                    //System.out.printf("| %-3s | %-6s | %-35s | %-35s |  %-15s %d\n", student.get_number(), student.get_matric(), student.get_name(), student.get_link(), student.get_github_id(), student.get_comment_number());
                    //counter_for_submitted++;
                }
            }
            System.out.println("____________________________________________________________________________________________");

            System.out.println();
            System.out.println(" * Students who have not submitted the GitHub account");
            System.out.println("______________________________________________________");
            System.out.printf("| %-3s | %-6s | %-35s |\n", "No.", "Matric", "Name");
            System.out.println("|_____|________|_____________________________________|");
            sheet1_row_counter = 0;
            i = 0;
            for (Student student : student_list) {
                if (student.get_link().equals("")) {
                    i++;
                    System.out.printf("| %-3s | %-6s | %-35s |\n", i, student.get_matric(), student.get_name());
                    Row row = get_sheet.getRow(sheet1_row_counter);
                    if (sheet1_row_counter == 0) {
                        Cell cell1 = row.createCell(5);
                        Cell cell2 = row.createCell(6);
                        Cell cell3 = row.createCell(7);
                        cell1.setCellValue(header2[0]);
                        cell2.setCellValue(header2[1]);
                        cell3.setCellValue(header2[2]);
                        sheet1_row_counter++;
                        row = get_sheet.getRow(sheet1_row_counter);
                    }
                    Cell cell1 = row.createCell(5);
                    Cell cell2 = row.createCell(6);
                    Cell cell3 = row.createCell(7);
                    cell1.setCellValue(i);
                    cell2.setCellValue(student.get_matric());
                    cell3.setCellValue(student.get_name());
                    sheet1_row_counter++;
                }
            }
            System.out.println("______________________________________________________\n");



            //sheet1_row_counter = 0;
            //i = 0;
            //int submission_number = 0;
//            System.out.println(" Issue 4 Submission");
//            System.out.println(" * Students who have submitted");
//            System.out.println("______________________________________________________");
//            System.out.printf("| %-3s | %-6s | %-35s |\n", "No.", "Matric", "Name");
//            System.out.println("|_____|________|_____________________________________|");
//            for (Student student : student_list) {
//                if (student.get_comment_number() != 0) {
//                    i++; //!!!!!
//                    System.out.printf("| %-3d | %-6s | %-35s |\n", i, student.get_matric(), student.get_name());
//                    Row row = get_sheet.getRow(sheet1_row_counter);
//                    if (sheet1_row_counter == 0) {
//                        Cell cell1 = row.createCell(9);
//                        Cell cell2 = row.createCell(10);
//                        Cell cell3 = row.createCell(11);
//                        cell1.setCellValue(header2[0]);
//                        cell2.setCellValue(header2[1]);
//                        cell3.setCellValue(header2[2]);
//                        sheet1_row_counter++;
//                        row = get_sheet.getRow(sheet1_row_counter);
//                    }
//                    Cell cell1 = row.createCell(9);
//                    Cell cell2 = row.createCell(10);
//                    Cell cell3 = row.createCell(11);
//                    cell1.setCellValue(i);
//                    cell2.setCellValue(student.get_matric());
//                    cell3.setCellValue(student.get_name());
//                    sheet1_row_counter++;

//                }
//            }
//            System.out.println("______________________________________________________");

            int comment_number = 0;
            int submission_number = 0;
            System.out.println(" Issue 4 Submission");
            System.out.println(" * Students who have not submitted and have submitted more then one");
            System.out.println("_________________________________________________________________");
            System.out.printf("| %-3s | %-6s | %-35s | %-3s |\n", "No.", "Matric", "Name", "Comments");
            System.out.println("|_____|________|_____________________________________|__________|");
            sheet1_row_counter = 0;
            i = 0;
            for (Student student : student_list) {
                if (student.get_comment_number() == 0 || student.get_comment_number() > 1) {
                    i++;
                    System.out.printf("| %-3s | %-6s | %-35s | %-3s      |\n", i, student.get_matric(), student.get_name(), student.get_comment_number());
                    Row row = get_sheet.getRow(sheet1_row_counter);
                    if (sheet1_row_counter == 0) {
                        Cell cell1 = row.createCell(9);
                        Cell cell2 = row.createCell(10);
                        Cell cell3 = row.createCell(11);
                        Cell cell4 = row.createCell(12);
                        cell1.setCellValue(header2[0]);
                        cell2.setCellValue(header2[1]);
                        cell3.setCellValue(header2[2]);
                        cell4.setCellValue(header2[3]);
                        sheet1_row_counter++;
                        row = get_sheet.getRow(sheet1_row_counter);
                    }
                    Cell cell1 = row.createCell(9);
                    Cell cell2 = row.createCell(10);
                    Cell cell3 = row.createCell(11);
                    Cell cell4 = row.createCell(12);
                    cell1.setCellValue(i);
                    cell2.setCellValue(student.get_matric());
                    cell3.setCellValue(student.get_name());
                    cell4.setCellValue(student.get_comment_number());
                    sheet1_row_counter++;
                }
                //counting comment number
                comment_number = comment_number + student.get_comment_number();
                //counting submission number
                if (student.get_comment_number() == 1 || student.get_comment_number() > 1){
                    submission_number++;
                }
            }
            System.out.println("_________________________________________________________________");
            System.out.printf(" Total number of comments    : %d\n", comment_number);
            System.out.printf(" Total number of submission  : %d\n", submission_number);
            System.out.printf(" Total number of students    : %d\n", student_list.size());
            //Auto size every column in excel file
            for (int j = 0; j<16; j++){
                get_sheet.autoSizeColumn(j);
            }

            FileOutputStream excel_file = new FileOutputStream("issues-result.xlsx");
            xlsx_workbook.write(excel_file);
            //xlsx_workbook.setHidden(false);
            excel_file.close();
            //xlsx_workbook.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

}
