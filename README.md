# Aceoffix7-React-SpringBoot-Simple

**Latest Version：7.0.0.2**

### 1. Introduction

The Aceoffix7-React-SpringBoot-Simple project demonstrates how to use the Aceoffix 7.0 product in a project with a separated front - end (React) and back - end (SpringBoot). This project showcases the simplest way to open, edit, and save Word files on web pages.

### 2. Project environmental prerequisites

   **React**: React 19 and  and later versions

   **Springboot**:Springboot2.6, JDK1.8

### 3. Steps for running the project

Use "git clone" or directly download the project's compressed package to your local machine and then decompress it.

- **front - end**
  - Open the `aceoffix7-react-simple-front` folder with a development tool like Visual Studio Code. Navigate into this folder and then use the `npm start` command to start and run the project.

- **back- end**

  - Create a new aceoffix system folder on the current server computer, for example: D:/aceoffix. (This folder will be used to store the authorization file “license.lic” generated after Aceoffix registration and the Aceoffix client program.)

  - Download the Aceoffix client program.
  
    [aceclientsetup_7.0.0.2.exe](https://github.com/aceoffix/aceoffix7-client/releases/download/v7.0.0.2/aceclientsetup_7.0.0.2.exe)
  
  - Copy the Aceoffix client program downloaded in the previous step to the newly created Aceoffix folder.
  
  - Run `aceoffix7-springboot-simple-back`  to see the sample effect.


### 4. Trial license key

- Aceoffix Standard V7.0 is 4ZDGS-FDZDK-WK18-YSJET

- Aceoffix Enterprise V7.0 is QA2JS-8C0PT-IKKJ-VTCC6

- Aceoffix Ultimate V7.0 is 9GRX9-VFFED-6NSN-ACVR1

### 5. How to integrate AceoffixV7 into your web project

- **front - end**

  - Install `js-aceoffix` in your project via the following command：**npm install js-aceoffix@7.0.0 --save-exact**

    > Note: Please ensure that the version number of the installed js-pageoffice library matches the first three digits of the version number specified in the PageOffice JAR package referenced in the backend project’s pom.xml file.

  - Add Aceoffix related configurations to the global interceptor in your project.

    ```react
    import axios from "axios";
    import { AceBrowser } from "js-aceoffix";
    
    const service = axios.create({
      baseURL: "/dev-api",
      timeout: 5000,
    });
    
    service.interceptors.request.use(
      (config) => {
        // Assume your token is stored in the cookie, then use this line of code. For the convenience of demonstration here, we use the constant token = "123".
        // const token = Cookies.get('token');
        const token = "123";
        if (token) {
          config.headers["Authorization"] = "Bearer " + token; 
    
          // The setup code for Aceoffix starts -------------------------------------------------
          // The global configuration of Aceoffix must be defined in this interceptor.
          AceBrowser.setProxyBaseAPI("/dev-api"); // Required. Set the backend proxy. The specific attribute values should be determined according to your actual development.
          AceBrowser.setHeader("Authorization", "Bearer " + token); // Required. Set headers for requests to the Aceoffix backend. You can call setHeader() multiple times to set more values. The specific attribute names and values should be determined according to your actual development.
          /**
            Front - end token storage solutions
            Solution 1: Use Cookies
            If your token is stored in cookies, Aceoffix will support storing the token via cookies by default. Therefore, you don't need to write any additional code.
            Solution 2: Use LocalStorage or SessionStorage
            If the token is stored in LocalStorage or SessionStorage, you must call the AceBrowser.setStorage() method.
           */
          //AceBrowser.setStorage("Admin-Token",getToken());// You can call setStorage() multiple times to set more values. The specific attribute names and values should be determined according to your actual development.
          // The setup code for Aceoffix ends -------------------------------------------------
        }
    
        return config;
      },
      (error) => {
        return Promise.reject(error);
      }
    );
    
    service.interceptors.response.use(
      (response) => {
        return response.data;
      },
      (error) => {
        return Promise.reject(error);
      }
    );
    
    export default service;
    ```

  - Trigger the pop - up of an Acebrowser window for editing Office documents by clicking on a hyperlink or a button. Let's assume that clicking a link in `App.js` pops up the Acebrowser, and the page containing the Aceoffix control is `ShowDoc.js`. (for example, `App.js`, which is referred to as the parent page in Aceoffix). When the hyperlink is clicked, call the `openWindow` method of the `Acebrowser` object to pop up an Aceoffix browser (`Acebrowser`) window to access `showDoc.js` and open the file online. The code is as follows.

    ```react
    const openWordFile = () => {
        try {
            // The third parameter of openWindow() is used to pass parameters (with no limit on parameter length) to the popped-up Aceoffix browser (POBrowser) window. It supports JSON - formatted strings.
            // For the convenience of demonstration here, we've passed two parameters, file_id and file_name. Please adjust according to your actual development.
            AceBrowser.openWindow("/showDoc", "width=1150px;height=900px;", paramString);
        } catch (error) {
            console.error("The request has gone wrong:", error);
        }
    };
    
    ```

  - Configure the access route for `showDoc.js`.

    ```react
      <Route path="/showDoc" element={<ShowDoc />} />
    ```

  -  Then we'll edit `showDoc.js`.

    ```react
    /* eslint-disable no-undef */
    import React, { useState, useEffect, useRef} from 'react';
    import service from '../api';
    const ShowDoc = () => {
      const [poHtmlCode, setPoHtmlCode] = useState('');
      const  open_params = useRef('');
      // The callback function for the initialization event of Aceoffix. You can add custom buttons here.
      const OnAceoffixCtrlInit = () => {
        aceoffixctrl.AddCustomToolButton("Save", "Save", 1);
        aceoffixctrl.AddCustomToolButton("SaveAs", "SaveAs", 5);
        aceoffixctrl.AddCustomToolButton("PrintSet", "PrintSet", 8);
        aceoffixctrl.AddCustomToolButton("PrintFile", "PrintFile", 6);
        aceoffixctrl.AddCustomToolButton("FullScreen", "IsFullScreen", 4);
        aceoffixctrl.AddCustomToolButton("Close", "Close", 9);
      };
    
      const Save = () => {
        // Use the SaveFilePage property to set the Controller route address of the backend save method. This address must start with "/", and you can also pass JSON string parameters to this route address. An example is as follows:
        let saveFileUrl = "/doc/saveFile";
        let paramValue = new URLSearchParams(open_params.current);// For simplicity, we directly use the parameters used during the opening process here.
        aceoffixctrl.SaveFilePage = `${saveFileUrl}?${paramValue.toString()}`;
        //aceoffixctrl.SaveFilePage = saveFileUrl;
        // Write your code here before saving.
        aceoffixctrl.WebSave();
        // Write your code here after saving. For example, you can check the save result using aceoffixctrl.CustomSaveResult.
      };
    
      const SaveAs = () => {
        aceoffixctrl.ShowDialog(3);
      };
      const  PrintSet = () => {
        aceoffixctrl.ShowDialog(5);
      };
      const PrintFile = () => {
        aceoffixctrl.ShowDialog(4);
      };
      const Close = () => {
        aceoffixctrl.CloseWindow();
      };
      const IsFullScreen = () => {
        aceoffixctrl.FullScreen = !aceoffixctrl.FullScreen;
      };
    
      const AfterDocumentOpened = () => {
      // Write the code here that will be automatically triggered after the document is opened.
      };
    
      const openFile = async () => {
        try {
            const response = await service.post('/doc/openFile', open_params.current);
            return response;
        } catch (error) {
            console.error('Error fetching file:', error);
            return null;
        }
    };
    
    useEffect(() => {
        open_params.current = JSON.parse(aceoffixctrl.WindowParams);
        openFile().then((response) => {
            if (response) {
                setPoHtmlCode(response);
            }
        });
    
        window.ACEPageMounted = { OnAceoffixCtrlInit, AfterDocumentOpened, Save, SaveAs, PrintSet, PrintFile, Close, IsFullScreen };
    
        return () => {
            delete window.ACEPageMounted;
        };
    }, []);
    
      return (
        <div className="showDoc">
         {/* This div is used to load the Aceoffix control. The height and width of the div determine the size and position of the control. */}
          <div
            style={{position:'absolute', width: '100%', height: '100%' }}
            dangerouslySetInnerHTML={{ __html: poHtmlCode }}
          />
        </div>
      );
    };
    
    export default ShowDoc
    ```

- back- end

  - Download the Aceoffix client program.

    [aceclientsetup_7.0.0.2.exe](https://github.com/aceoffix/aceoffix7-client/releases/download/v7.0.0.2/aceclientsetup_7.0.0.2.exe)

  - Copy the Aceoffix client program downloaded in the previous step to the newly created Aceoffix folder.

  -  Introduce the Aceoffix dependency in the `pom.xml` file of your project using the following code. The `aceoffix.jar`
      has been published to the [Maven Central Repository](https://central.sonatype.com/artifact/com.acesoftcorp/aceoffix).It is recommended to use the latest version.

    > Note: It is recommended to use the latest version of Aceoffix Jar from the current Maven Central Repository.

    ```xml
    <dependency>
        <groupId>com.acesoftcorp</groupId>
        <artifactId>aceoffix</artifactId>
        <version>7.0.0.2-javax</version>
    </dependency>
    ```

    If you are using SpringBoot 3, please use the following configuration code.

    ```xml
    <dependency>
        <groupId>com.acesoftcorp</groupId>
        <artifactId>aceoffix</artifactId>
        <version>7.0.0.2</version>
    </dependency>
    ```

  - Then, write the following server code in "controllers/DocumentController.java".

    ```java
    @RequestMapping(value = "/openFile", method = org.springframework.web.bind.annotation.RequestMethod.POST)
    public String openFile(HttpServletRequest request, @RequestBody Map<String, Object> params) {
        AceoffixCtrl aceCtrl = new AceoffixCtrl(request);
        String file_name = (String) params.get("file_name");
        aceCtrl.webOpen("file://" + dir + file_name, OpenModeType.docNormalEdit, "Luna");
        return aceCtrl.getHtml();
    }
    ```

  - Add a new function called Save in  "controllers/DocumentController.java"  if your user wants to save document.

    ```java
    @RequestMapping(value = "/saveFile",method = org.springframework.web.bind.annotation.RequestMethod.POST)
    public void saveFile(HttpServletRequest request, HttpServletResponse response,@RequestParam String file_name) {
        FileSaver fs = new FileSaver(request, response);
        fs.saveToFile(dir + file_name);
        fs.close();
    }
    ```

- Run the front - end project and the back - end project respectively. Then, access the front - end React project in the browser's address bar. Follow the prompts to install the Aceoffix V7 client. Once the registration dialog box appears, enter the license key of Aceoffix V7 to complete the registration.

