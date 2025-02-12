package com.acesoft.aceoffix7springbootsimple.controller;

import com.acesoftcorp.aceoffix.AceoffixCtrl;
import com.acesoftcorp.aceoffix.FileSaver;
import com.acesoftcorp.aceoffix.OpenModeType;
import com.acesoft.aceoffix7springbootsimple.util.GetDirPathUtil;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping(value = "/doc")
public class DocumentController {
    //Get the disk path of the doc directory
    private final String dir = GetDirPathUtil.getDirPath() + "/static/doc/";
    @RequestMapping(value = "/openFile", method = org.springframework.web.bind.annotation.RequestMethod.POST)
    public String openFile(HttpServletRequest request, @RequestBody Map<String, Object> params) {
        AceoffixCtrl aceCtrl = new AceoffixCtrl(request);
        String file_name = (String) params.get("file_name");
        aceCtrl.webOpen("file://" + dir + file_name, OpenModeType.docNormalEdit, "Luna");
        return aceCtrl.getHtml();
    }

    @RequestMapping(value = "/saveFile",method = org.springframework.web.bind.annotation.RequestMethod.POST)
    public void saveFile(HttpServletRequest request, HttpServletResponse response,@RequestParam String file_name) {
        FileSaver fs = new FileSaver(request, response);
        fs.saveToFile(dir + file_name);
        fs.close();
    }
}



