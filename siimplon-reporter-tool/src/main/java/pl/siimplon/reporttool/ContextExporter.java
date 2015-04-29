package pl.siimplon.reporttool;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import pl.siimplon.reporter.ReportContext;
import pl.siimplon.reporter.report.Report;
import pl.siimplon.reporter.report.record.Record;
import pl.siimplon.reporter.report.style.Style;
import pl.siimplon.reporter.report.value.Value;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContextExporter {

    private static final Map<Style.Color, Short> colorMap;

    static {
        colorMap = new HashMap<Style.Color, Short>();

        colorMap.put(Style.Color.DEFAULT, IndexedColors.WHITE.getIndex());
        colorMap.put(Style.Color.BLUE, IndexedColors.BLUE.getIndex());
        colorMap.put(Style.Color.LIGHT_BLUE, IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        colorMap.put(Style.Color.RED, IndexedColors.RED.getIndex());
        colorMap.put(Style.Color.WHITE, IndexedColors.WHITE.getIndex());
        colorMap.put(Style.Color.YELLOW, IndexedColors.YELLOW.getIndex());
        colorMap.put(Style.Color.GREEN, IndexedColors.GREEN.getIndex());
        colorMap.put(Style.Color.LIGHT_ORANGE, IndexedColors.LIGHT_ORANGE.getIndex());
        colorMap.put(Style.Color.GREY_40_PERCENT, IndexedColors.GREY_40_PERCENT.getIndex());
        colorMap.put(Style.Color.LIGHT_TURQUOISE, IndexedColors.LIGHT_TURQUOISE.getIndex());
        colorMap.put(Style.Color.TURQUOISE, IndexedColors.TURQUOISE.getIndex());
        colorMap.put(Style.Color.BRIGHT_GREEN, IndexedColors.BRIGHT_GREEN.getIndex());
        colorMap.put(Style.Color.VIOLET, IndexedColors.VIOLET.getIndex());
    }

    private final ReportContext reportContext;

    public ContextExporter(ReportContext reportContext) {
        this.reportContext = reportContext;
    }

    public void export(List<String> reports, String sheetName, File file) throws IOException {
        HSSFWorkbook workbook = getWorkbook(file);
        HSSFSheet sheet = makeSheet(workbook, sheetName);
        for (String report : reports) {
            export(report, sheet);
        }
        FileOutputStream fos = new FileOutputStream(file);
        workbook.write(fos);
        fos.close();
    }

    private void export(String reportName, HSSFSheet sheet) {
        Report report = reportContext.getReport(reportName);
        int lastRow = sheet.getLastRowNum() + 1;
        for (int i = 0; i < report.getRecords().size(); i++) {
            HSSFRow row = sheet.createRow(lastRow + i);
            Record record = report.getRecords().get(i);
            for (int j = 0; j < record.getLength(); j++) {
                Value value = record.getValue(j);
                HSSFCell cell = row.createCell(j);
                switch (value.getType()) {
                    case LITERAL:
                        cell.setCellValue(value.toString());
//                        cell.setCellStyle(createColorStringStyle(Style.Color.WHITE, sheet.getWorkbook()));
                        break;
                    case NUMBER:
                        cell.setCellValue(((Double) value.getValue()));
//                        cell.setCellStyle(createColorNumberStyle(Style.Color.WHITE, sheet.getWorkbook()));
                        break;
                    case DICTIONARY:
                        cell.setCellValue(value.toString());
                        break;
                }
            }
        }
    }

    private CellStyle createColorStringStyle(Style.Color color, HSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

        style.setFillForegroundColor(colorMap.get(color));
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        return style;
    }

    private CellStyle createColorNumberStyle(Style.Color color, HSSFWorkbook workbook) {
        CellStyle s = createColorStringStyle(color, workbook);
        HSSFDataFormat format = workbook.createDataFormat();
        s.setDataFormat(format.getFormat("0.00"));
        return s;
    }

    private HSSFSheet makeSheet(HSSFWorkbook workbook, String sheetName) {
        HSSFSheet sheet = workbook.getSheet(sheetName);
        if (sheet != null) {
            int sheetIndex = workbook.getSheetIndex(sheetName);
            workbook.removeSheetAt(sheetIndex);
        }
        return workbook.createSheet(sheetName);
    }

    private HSSFWorkbook getWorkbook(File file) throws IOException {
        if (file.exists()) {
            FileInputStream fis = new FileInputStream(file);
            HSSFWorkbook workbook = new HSSFWorkbook(fis);
            fis.close();
            return workbook;
        } else {
            return new HSSFWorkbook();
        }
    }
}
