package com.example.edu.menutb.view.configure;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import com.example.edu.menutb.R;

public class TermsActivity extends AppCompatActivity {

    private WebView webViewTerms;
    Button buttonAcceptTerms;
    Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_terms);

        intent = getIntent();
        buttonAcceptTerms = (Button) findViewById(R.id.buttonAcceptTerms);
        Bundle extras = getIntent().getExtras();
        if (extras.getString("aparecer").equals("false")) {
            buttonAcceptTerms.setText(getString(R.string.close));
            buttonAcceptTerms.setTextColor(Color.WHITE);
        }
        buttonAcceptTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        webViewTerms = (WebView) findViewById(R.id.webViewTerms);
        WebSettings webSettings = webViewTerms.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);
        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\n" +
                "    <link href=\"https://fonts.googleapis.com/css?family=Amatic+SC\" rel=\"stylesheet\">\n" +
                "    <title>Tully</title>\n" +
                "</head>\n" +
                "\n" +
                "<style>\n" +
                "    body {\n" +
                "        font-size: 1em;\n" +
                "        background-color: #0097a7;\n" +
                "    }\n" +
                "    h2, h4, p, ul{\n" +
                "        margin: 0px;\n" +
                "        margin-bottom: 2px;\n" +
                "    }\n" +
                "    h2 {\n" +
                "        text-align: center;\n" +
                "        font-family: 'Amatic SC', cursive;\n" +
                "        font-size: 1.4em;\n" +
                "    }\n" +
                "    div {\n" +
                "        color: white;\n" +
                "    }\n" +
                "</style>\n" +
                "\n" +
                "<body>\n" +
                "    <div>\n" +
                "        <h2>TULLY'S PRIVACY POLICY</h2> \n" +
                "        <p>This privacy policy governs your use of the software application Tully for mobile devices\n" +
                "            that was created by Tully. The Application is free</p> \n" +
                "        <h4>What information does the Application obtain and how is it used?</h4> \n" +
                "        <p><strong>User Provided Information</strong></p> \n" +
                "        <p>The Application obtains the information you provide when you download and register. Registration with us is optional.\n" +
                "            However, please keep in mind that you may not be able to use some of the features offered by the Application unless you register with us.</p>\n" +
                "        <p>When you register with us and use the Application, you generally provide:<br>\n" +
                "            (a) your name, email address, city, country, username, password and a profile photo (optional);<br>\n" +
                "            (b) transaction-related information, such as when you make purchases, respond to any offers, or download or use applications from us; (not implemented yet)<br>\n" +
                "            (c) information you provide us when you contact us for help (tully_tcc@hotmail.com);<br>\n" +
                "            (d) credit card information for purchase and use of the Application (not implemented yet), and;<br>\n" +
                "            (e) information you enter into our system when using the Application, such as positioning information.</p> \n" +
                "        <p>We may also use the information you provided us to contact you from time to time to provide you with important information, \n" +
                "            required notices and marketing promotions.</p> \n" +
                "        <p><strong>Automatically Collected Information</strong></p>\n" +
                "        <p>In addition, the Application may collect certain information automatically, including, but not limited to, the type of mobile device you use,\n" +
                "            your mobile devices unique device ID, the IP address of your mobile device, your mobile operating system, the type of mobile Internet browsers you use\n" +
                "            and information about the way you use the Application.</p> \n" +
                "        <h4>Does the Application collect precise real time location information of the device?</h4> \n" +
                "        <p>When you visit the mobile application, we may use GPS technology (or other similar technology) to determine your current location in order to determine\n" +
                "            the city you are located within and display a location map with relevant advertisements. We will not share your current location with other users or partners.</p> \n" +
                "        <p>If you do not want us to use your location for the purposes set forth above, you should turn off the location services for the mobile application located\n" +
                "            in your account settings or in your mobile phone settings and/or within the mobile application.</p> \n" +
                "        <h4>Do third parties see and/or have access to information obtained by the Application?</h4> \n" +
                "        <p>Yes. We will share your information with third parties only in the ways that are described in this privacy statement.</p> \n" +
                "        <p>We may disclose User Provided and Automatically Collected Information:</p> \n" +
                "        <ul> \n" +
                "        <li>as required by law, such as to comply with a subpoena, or similar legal process;</li>\n" +
                "        <li>when we believe in good faith that disclosure is necessary to protect our rights, protect your safety or the safety of others,\n" +
                "            investigate fraud, or respond to a government request;</li>\n" +
                "        <li>with our trusted services providers who work on our behalf, do not have an independent use of the information we disclose to them,\n" +
                "            and have agreed to adhere to the rules set forth in this privacy statement.</li>\n" +
                "        <li>if Tully is involved in a merger, acquisition, or sale of all or a portion of its assets, you will be notified via email and/or a prominent notice\n" +
                "            on our Web site of any change in ownership or uses of this information, as well as any choices you may have regarding this information.</li>\n" +
                "        </ul>\n" +
                "        <h4>What are my opt-out rights?</h4>\n" +
                "        <p>You can stop all collection of information by the Application easily by uninstalling the Application. You may use the standard uninstall processes\n" +
                "            as may be available as part of your mobile device or via the mobile application marketplace or network. You can also request to opt-out via email,\n" +
                "            at tully_tcc@hotmail.com.</p>\n" +
                "        <h4><strong>Data Retention Policy, Managing Your Information</strong></h4>\n" +
                "        <p>We will retain User Provided data for as long as you use the Application and for a reasonable time thereafter. We will retain Automatically Collected\n" +
                "            information for up to 24 months and thereafter may store it in aggregate. If you’d like us to delete User Provided Data that you have provided via\n" +
                "            the Application, please contact us at tully_tcc@hotmail. We will respond in a reasonable time. Please note that some or all of the User Provided Data\n" +
                "            may be required in order for the Application to function properly.</p>\n" +
                "        <h4><strong>Children</strong></h4>\n" +
                "        <p>We do not use the Application to knowingly solicit data from or market to children under the age of 13. If a parent or guardian becomes aware that\n" +
                "            his or her child has provided us with information without their consent, he or she should contact us at tully_tcc@hotmail.com. We will delete such\n" +
                "            information from our files within a reasonable time.</p>\n" +
                "        <h4><strong>Security</strong></h4>\n" +
                "        <p>We are concerned about safeguarding the confidentiality of your information. We provide physical, electronic, and procedural safeguards to protect\n" +
                "            information we process and maintain. For example, we limit access to this information to authorized employees and contractors who need to know that\n" +
                "            information in order to operate, develop or improve our Application. Please be aware that, although we endeavor provide reasonable security for\n" +
                "            information we process and maintain, no security system can prevent all potential security breaches.</p>\n" +
                "        <h4><strong>Changes</strong></h4>\n" +
                "        <p>This Privacy Policy may be updated from time to time for any reason. We will notify you of any changes to our Privacy Policy by posting the new\n" +
                "            Privacy Policy inside the app and informing you via email. You are advised to consult this Privacy Policy regularly for any changes, as continued\n" +
                "            use is deemed approval of all changes. You can check the history of this policy by visiting the configuration menu.</p>\n" +
                "        <h4><strong>Your Consent</strong></h4>\n" +
                "        <p>By using the Application, you are consenting to our processing of your information as set forth in this Privacy Policy now and as amended by us.\n" +
                "            \"Processing” means using cookies on a computer/hand held device or using or touching information in any way, including, but not limited to,\n" +
                "            collecting, storing, deleting, using, combining and disclosing information, all of which activities will take place in Brazil.</p> \n" +
                "        <h4>Contact us</h4>\n" +
                "        <p>If you have any questions regarding privacy while using the Application, or have questions about our practices, please contact us via email at\n" +
                "            tully_tcc@hotmail.com.</p>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
        webViewTerms.loadData(html, "text/html", null);
    }
}
