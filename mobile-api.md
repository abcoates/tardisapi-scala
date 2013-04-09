Tardis Mobile API
=================

1. During this testing phase, to set up a user/patient for testing, please go to the home page and following the 'Click to register' link.
2. Results are returned as JSON when the MIME type in the '`Accept`' header of the HTTP request is set to '`application/json`'.  Otherwise HTML is returned.

----

Logging In and Out
------------------
    POST /mobile/v1/login
* After a successful login, a session cookie is set.  Cookies must be enabled for the API to work.
* Cookies are not persistent, they only last until the end of the browser session.

<table>
<thead>
<tr><th>Form Parameter</th><th>Description</th></tr>
</thead>
<tbody>
<tr><td><code>email</code></td><td>E-mail address provided by the user when registering.</td></tr>
<tr><td><code>password</code></td><td>Password provided by the user when registering.</td></tr>
</tbody>
</table>
<br/>

<table>
<thead>
<tr><th>JSON Result Field</th><th>Type</th><th>Description</th></tr>
</thead>
<tbody>
<tr><td><code>status</code></td><td>String = 'OK' or 'FAIL'</td><td>Whether the login request succeeded or failed.</td></tr>
<tr><td><code>userid</code></td><td>Positive Integer</td><td>If 'status' is 'OK', the ID number of the person who logged in.</td></tr>
<tr><td><code>isPatient</code></td><td>Boolean</td><td>If 'status' is 'OK', whether the person who logged in is a patient.</td></tr>
<tr><td><code>isDoctor</code></td><td>Boolean</td><td>If 'status' is 'OK', whether the person who logged in is a doctor.</td></tr>
<tr><td><code>isAdmin</code></td><td>Boolean</td><td>If 'status' is 'OK', whether the person who logged in is an administrator.</td></tr>
<tr><td><code>errorCode</code></td><td>String</td><td>If 'status' is 'FAIL', the error code.</td></tr>
<tr><td><code>errorDetails</code></td><td>String</td><td>If 'status' is 'FAIL', a human readable description of the error.</td></tr>
<tr><td><code>email</code></td><td>String</td><td>If 'status' is 'FAIL', e-mail address from the form post, if available.</td></tr>
<tr><td><code>password</code></td><td>String</td><td>If 'status' is 'FAIL', password from the form post, if available.</td></tr>
</tbody>
</table>
<br/>

<table>
<tbody>
<tr><th>Windows example</th><td><pre>set URLPREFIX=http://localhost:9000
set HTTPHEADERS=-LH "Accept: application/json"
curl %HTTPHEADERS% --data "email=%EMAIL%&password=%PASSWORD%" "%URLPREFIX%/mobile/v1/login"</pre></td></tr>
<tr><th>Mac/Linux example (untested)</th><td><pre>URLPREFIX='http://localhost:9000'
HTTPHEADERS=-LH "Accept: application/json"
curl $HTTPHEADERS --data "email=$EMAIL&password=$PASSWORD" "$URLPREFIX/mobile/v1/login"</pre></td></tr>
</tbody>
</table>
<br/>

    GET /mobile/v1/logout
* After a successful logout, cookies are cleared.

<table>
<thead>
<tr><th>Form Parameter</th><th>Description</th></tr>
</thead>
<tbody>
<tr><td>N/A</td><td>&nbsp;</td></tr>
</tbody>
</table>
<br/>

<table>
<thead>
<tr><th>JSON Result Field</th><th>Type</th><th>Description</th></tr>
</thead>
<tbody>
<tr><td><code>status</code></td><td>String = 'OK' or 'FAIL'</td><td>Whether the logout request succeeded or failed.</td></tr>
</tbody>
</table>
<br/>

<table>
<tbody>
<tr><th>Windows example</th><td><pre>set URLPREFIX=http://localhost:9000
set HTTPHEADERS=-LH "Accept: application/json"
curl %HTTPHEADERS% "%URLPREFIX%/mobile/v1/logout"</pre></td></tr>
<tr><th>Mac/Linux example (untested)</th><td><pre>URLPREFIX='http://localhost:9000'
HTTPHEADERS=-LH "Accept: application/json"
curl "$URLPREFIX/mobile/v1/logout"</pre></td></tr>
</tbody>
</table>
<br/>

----

Retrieving patient details
---------------------------

    GET     /mobile/v1/patients/:userid
* Retrieves details for the given patient, includes symptom and event IDs.

<table>
<thead>
<tr><th>Form Parameter</th><th>Description</th></tr>
</thead>
<tbody>
<tr><td>N/A</td><td>&nbsp;</td></tr>
</tbody>
</table>
<br/>

<table>
<thead>
<tr><th>JSON Result Field</th><th>Type</th><th>Description</th></tr>
</thead>
<tbody>
<tr><td><code>status</code></td><td>String = 'OK' or 'FAIL'</td><td>Whether the logout request succeeded or failed.</td></tr>
<tr><td><code>userid</code></td><td>Positive Integer</td><td>If 'status' is 'OK', the ID number of the patient.</td></tr>
<tr><td><code>username</code></td><td>String</td><td>If 'status' is 'OK', the name of the patient.</td></tr>
<tr><td><code>email</code></td><td>String</td><td>If 'status' is 'OK', the e-mail address of the patient.</td></tr>
<tr><td><code>symptoms</code></td><td>Array of Positive Integer</td><td>If 'status' is 'OK', the ID numbers of the patient's symptoms.</td></tr>
<tr><td><code>events</code></td><td>Array of Positive Integer</td><td>If 'status' is 'OK', the ID numbers of the patient's events.</td></tr>
</tbody>
</table>
<br/>

<table>
<tbody>
<tr><th>Windows example</th><td><pre>set URLPREFIX=http://localhost:9000
set USERID=1
set HTTPHEADERS=-LH "Accept: application/json"
curl %HTTPHEADERS% "%URLPREFIX%/mobile/v1/patients/%USERID%"</pre></td></tr>
<tr><th>Mac/Linux example (untested)</th><td><pre>URLPREFIX='http://localhost:9000'
USERID=1
HTTPHEADERS=-LH "Accept: application/json"
curl "$URLPREFIX/mobile/v1/patients/$USERID"</pre></td></tr>
</tbody>
</table>
<br/>

----

Retrieving patient symptoms
---------------------------

    GET     /mobile/v1/patients/:userid/symptoms
* Retrieves symptom IDs for the given patient.

<table>
<thead>
<tr><th>Form Parameter</th><th>Description</th></tr>
</thead>
<tbody>
<tr><td>N/A</td><td>&nbsp;</td></tr>
</tbody>
</table>
<br/>

<table>
<thead>
<tr><th>JSON Result Field</th><th>Type</th><th>Description</th></tr>
</thead>
<tbody>
<tr><td><code>status</code></td><td>String = 'OK' or 'FAIL'</td><td>Whether the logout request succeeded or failed.</td></tr>
<tr><td><code>userid</code></td><td>Positive Integer</td><td>If 'status' is 'OK', the ID number of the patient.</td></tr>
<tr><td><code>username</code></td><td>String</td><td>If 'status' is 'OK', the name of the patient.</td></tr>
<tr><td><code>email</code></td><td>String</td><td>If 'status' is 'OK', the e-mail address of the patient.</td></tr>
<tr><td><code>symptoms</code></td><td>Array of Positive Integer</td><td>If 'status' is 'OK', the ID numbers of the patient's symptoms.</td></tr>
</tbody>
</table>
<br/>

<table>
<tbody>
<tr><th>Windows example</th><td><pre>set URLPREFIX=http://localhost:9000
set USERID=1
set HTTPHEADERS=-LH "Accept: application/json"
curl %HTTPHEADERS% "%URLPREFIX%/mobile/v1/patients/%USERID%/symptoms"</pre></td></tr>
<tr><th>Mac/Linux example (untested)</th><td><pre>URLPREFIX='http://localhost:9000'
USERID=1
HTTPHEADERS=-LH "Accept: application/json"
curl "$URLPREFIX/mobile/v1/patients/$USERID/symptoms"</pre></td></tr>
</tbody>
</table>
<br/>

    GET     /mobile/v1/patients/:userid/symptoms/:id
* Retrieves details of the given symptom for the given patient.

<table>
<thead>
<tr><th>Form Parameter</th><th>Description</th></tr>
</thead>
<tbody>
<tr><td>N/A</td><td>&nbsp;</td></tr>
</tbody>
</table>
<br/>

<table>
<thead>
<tr><th>JSON Result Field</th><th>Type</th><th>Description</th></tr>
</thead>
<tbody>
<tr><td><code>status</code></td><td>String = 'OK' or 'FAIL'</td><td>Whether the logout request succeeded or failed.</td></tr>
<tr><td><code>symptomid</code></td><td>Positive Integer</td><td>If 'status' is 'OK', the ID number of the symptom.</td></tr>
<tr><td><code>userid</code></td><td>Positive Integer</td><td>If 'status' is 'OK', the ID number of the patient.</td></tr>
<tr><td><code>whichsymptom</code></td><td>String</td><td>If 'status' is 'OK', the description of the patient's symptom.</td></tr>
<tr><td><code>whensymptom</code></td><td>Date/Time</td><td>If 'status' is 'OK', the date and time of the patient's symptom.  The time is to the nearest quarter hour, so the minutes are one of '0', '15', '30' or '45', and the seconds are '0'.</td></tr>
</tbody>
</table>
<br/>

<table>
<tbody>
<tr><th>Windows example</th><td><pre>set URLPREFIX=http://localhost:9000
set USERID=1
set SYMPTOMID=1
set HTTPHEADERS=-LH "Accept: application/json"
curl %HTTPHEADERS% "%URLPREFIX%/mobile/v1/patients/%USERID%/symptoms/%SYMPTOMID%"</pre></td></tr>
<tr><th>Mac/Linux example (untested)</th><td><pre>URLPREFIX='http://localhost:9000'
USERID=1
SYMPTOMID=1
HTTPHEADERS=-LH "Accept: application/json"
curl "$URLPREFIX/mobile/v1/patients/$USERID/symptoms/$SYMPTOMID"</pre></td></tr>
</tbody>
</table>
<br/>

----

Retrieving patient events
-------------------------

    GET     /mobile/v1/patients/:userid/events
* Retrieves event IDs for the given patient.

<table>
<thead>
<tr><th>Form Parameter</th><th>Description</th></tr>
</thead>
<tbody>
<tr><td>N/A</td><td>&nbsp;</td></tr>
</tbody>
</table>
<br/>

<table>
<thead>
<tr><th>JSON Result Field</th><th>Type</th><th>Description</th></tr>
</thead>
<tbody>
<tr><td><code>status</code></td><td>String = 'OK' or 'FAIL'</td><td>Whether the logout request succeeded or failed.</td></tr>
<tr><td><code>userid</code></td><td>Positive Integer</td><td>If 'status' is 'OK', the ID number of the patient.</td></tr>
<tr><td><code>username</code></td><td>String</td><td>If 'status' is 'OK', the name of the patient.</td></tr>
<tr><td><code>email</code></td><td>String</td><td>If 'status' is 'OK', the e-mail address of the patient.</td></tr>
<tr><td><code>events</code></td><td>Array of Positive Integer</td><td>If 'status' is 'OK', the ID numbers of the patient's events.</td></tr>
</tbody>
</table>
<br/>

<table>
<tbody>
<tr><th>Windows example</th><td><pre>set URLPREFIX=http://localhost:9000
set USERID=1
set HTTPHEADERS=-LH "Accept: application/json"
curl %HTTPHEADERS% "%URLPREFIX%/mobile/v1/patients/%USERID%/events"</pre></td></tr>
<tr><th>Mac/Linux example (untested)</th><td><pre>URLPREFIX='http://localhost:9000'
USERID=1
HTTPHEADERS=-LH "Accept: application/json"
curl "$URLPREFIX/mobile/v1/patients/$USERID/events"</pre></td></tr>
</tbody>
</table>
<br/>

    GET     /mobile/v1/patients/:userid/events/:id
* Retrieves details of the given event for the given patient.

<table>
<thead>
<tr><th>Form Parameter</th><th>Description</th></tr>
</thead>
<tbody>
<tr><td>N/A</td><td>&nbsp;</td></tr>
</tbody>
</table>
<br/>

<table>
<thead>
<tr><th>JSON Result Field</th><th>Type</th><th>Description</th></tr>
</thead>
<tbody>
<tr><td><code>status</code></td><td>String = 'OK' or 'FAIL'</td><td>Whether the logout request succeeded or failed.</td></tr>
<tr><td><code>eventid</code></td><td>Positive Integer</td><td>If 'status' is 'OK', the ID number of the event.</td></tr>
<tr><td><code>userid</code></td><td>Positive Integer</td><td>If 'status' is 'OK', the ID number of the patient.</td></tr>
<tr><td><code>eventname</code></td><td>String</td><td>If 'status' is 'OK', the description of the patient's event.</td></tr>
<tr><td><code>eventtime</code></td><td>Date</td><td>If 'status' is 'OK', the date patient's event.  This is a date only without a time.</td></tr>
</tbody>
</table>
<br/>

<table>
<tbody>
<tr><th>Windows example</th><td><pre>set URLPREFIX=http://localhost:9000
set USERID=1
set EVENTID=1
set HTTPHEADERS=-LH "Accept: application/json"
curl %HTTPHEADERS% "%URLPREFIX%/mobile/v1/patients/%USERID%/events/%EVENTID%"</pre></td></tr>
<tr><th>Mac/Linux example (untested)</th><td><pre>URLPREFIX='http://localhost:9000'
USERID=1
EVENTID=1
HTTPHEADERS=-LH "Accept: application/json"
curl "$URLPREFIX/mobile/v1/patients/$USERID/events/EVENTID"</pre></td></tr>
</tbody>
</table>
<br/>

----

Adding a new symptom
------------------
    POST /mobile/v1/patients/:userid/symptoms
* <code>:userid</code> is the user ID, as returned by the login, for the patient for whom the new symptom is being added.

<table>
<thead>
<tr><th>Form Parameter</th><th>Type</th><th>Description</th></tr>
</thead>
<tbody>
<tr><td><code>whichsymptom</code></td><td>String</td><td>Text description of the symptom.</td></tr>
<tr><td><code>whensymptom</code></td><td>Date/Time</td><td>Date/time at which the symptom occurred, to the nearest quarter hour.</td></tr>
</tbody>
</table>
<br/>

<table>
<thead>
<tr><th>JSON Result Field</th><th>Type</th><th>Description</th></tr>
</thead>
<tbody>
<tr><td><code>status</code></td><td>String = 'OK' or 'FAIL'</td><td>Whether the request to add a symptom succeeded or failed.</td></tr>
<tr><td><code>userid</code></td><td>Positive Integer</td><td>If 'status' is 'OK', the user ID of the patient.</td></tr>
<tr><td><code>symptomid</code></td><td>Positive Integer</td><td>If 'status' is 'OK', the ID of the added symptom.</td></tr>
<tr><td><code>errorCode</code></td><td>String</td><td>If 'status' is 'FAIL', the error code.</td></tr>
<tr><td><code>errorDetails</code></td><td>String</td><td>If 'status' is 'FAIL', a human readable description of the error.</td></tr>
</tbody>
</table>
<br/>

<table>
<tbody>
<tr><th>Windows example</th><td><pre>set URLPREFIX=http://localhost:9000
set USERID=1
set SYMPTOMTEXT=some text
set SYMPTOMDATE=2012-12-31 12:15:00
set HTTPHEADERS=-LH "Accept: application/json"
curl %HTTPHEADERS% --data "whichsymptom=%SYMPTOMTEXT%&whensymptom=%SYMPTOMDATE%" "%URLPREFIX%/mobile/v1/patients/%USERID%/symptoms"</pre></td></tr>
<tr><th>Mac/Linux example (untested)</th><td><pre>URLPREFIX='http://localhost:9000'
USERID=1
SYMPTOMTEXT='some text'
SYMPTOMDATE='2012-12-31 12:15:00'
HTTPHEADERS=-LH "Accept: application/json"
curl $HTTPHEADERS --data "whichsymptom=$SYMPTOMTEXT&whensymptom=$SYMPTOMDATE" "$URLPREFIX/mobile/v1/patients/$USERID/symptoms"</pre></td></tr>
</tbody>
</table>
<br/>

----

Adding a new event
------------------
    POST /mobile/v1/patients/:userid/events
* <code>:userid</code> is the user ID, as returned by the login, for the patient for whom the new event is being added.

<table>
<thead>
<tr><th>Form Parameter</th><th>Type</th><th>Description</th></tr>
</thead>
<tbody>
<tr><td><code>eventname</code></td><td>String</td><td>Text description of the event.</td></tr>
<tr><td><code>eventtime</code></td><td>Date</td><td>Date on which the event occurred.</td></tr>
</tbody>
</table>
<br/>

<table>
<thead>
<tr><th>JSON Result Field</th><th>Type</th><th>Description</th></tr>
</thead>
<tbody>
<tr><td><code>status</code></td><td>String = 'OK' or 'FAIL'</td><td>Whether the request to add a symptom succeeded or failed.</td></tr>
<tr><td><code>userid</code></td><td>Positive Integer</td><td>If 'status' is 'OK', the user ID of the patient.</td></tr>
<tr><td><code>eventid</code></td><td>Positive Integer</td><td>If 'status' is 'OK', the ID of the added event.</td></tr>
<tr><td><code>errorCode</code></td><td>String</td><td>If 'status' is 'FAIL', the error code.</td></tr>
<tr><td><code>errorDetails</code></td><td>String</td><td>If 'status' is 'FAIL', a human readable description of the error.</td></tr>
</tbody>
</table>
<br/>

<table>
<tbody>
<tr><th>Windows example</th><td><pre>set URLPREFIX=http://localhost:9000
set USERID=1
set EVENTNAME=some text
set EVENTTIME=2012-12-31
set HTTPHEADERS=-LH "Accept: application/json"
curl %HTTPHEADERS% --data "eventname=%EVENTNAME%&eventtime=%EVENTTIME%" "%URLPREFIX%/mobile/v1/patients/%USERID%/events"</pre></td></tr>
<tr><th>Mac/Linux example (untested)</th><td><pre>URLPREFIX='http://localhost:9000'
USERID=1
EVENTNAME='some text'
EVENTTIME='2012-12-31'
HTTPHEADERS=-LH "Accept: application/json"
curl $HTTPHEADERS --data "eventname=$EVENTNAME&eventtime=$EVENTTIME" "$URLPREFIX/mobile/v1/patients/$USERID/events"</pre></td></tr>
</tbody>
</table>
<br/>

----
