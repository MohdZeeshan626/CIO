/*Disclaimer: IMPORTANT: This Colworx software is proprietary confidential property owned exclusively by Colworx Tech Inc. ("CTI") Without prior written consent and compensation, Colworx expressly forbids the use, installation, modification or redistribution of this CTI software.
 CTI forbids all parties, without a non-exclusive license, under CTI's copyrights in this original CTI software (the "CTI Software"), to use, reproduce, modify and redistribute the CTI Software, with or without modifications, in source and/or binary forms. In all cases, you must retain this notice and the following text and  Disclaimers in all such redistributions of the CTI Software.
 The CTI Software is provided by CTI on an "AS IS" basis. CTI MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE CTI SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.
 IN NO EVENT SHALL CTI BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION). ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF CTI HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 Copyright (c) 2015 Colworx Tech Inc. All rights reserved.*/

package com.webmobi.gecmedia.Views;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.webmobi.gecmedia.CustomViews.DialogCustom;

public class TokenExpireAlertReceived extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        displayAlert(
                getIntent().getExtras().getString("keyAlert"),
                getIntent().getExtras().getString("keyMessage"));

    }

    private void displayAlert(String alert, String message) {

        DialogCustom dc = new DialogCustom(alert, message, TokenExpireAlertReceived.this);

        dc.showDialog();
    }

}