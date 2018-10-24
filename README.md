# VdAB Mail Nodes
### Overview
This repository includes the source and libraries associated with the different mail nodes 
including nodes to read email, write mail and to send text using email.
<b> Currently these nodes are included in the standard VDAB server but the source is provided 
for anyone wishing to enhance these features.</b>

| | |
|  --- |  :---: |
| Application Page    | NA |
| Demo Web Link   | NA |

### Features
<ul>
<li>The <i>SendMailService</i> node creates and sends emails including attachments.
<li>The <i>GetMailService</i> node reads new email and creates appropriate VDAB events.
<li>The <i>SendTextService</i> sends a text message to a phone via email.
</ul>

### Loading the the Package
The current or standard version can be loaded directly using the VDAB Android Client following the directions
for [Adding Packages](https://vdabtec.com/vdab/docs/VDABGUIDE_AddingPackages.pdf) and selecting the <i>MailNodes</i> package.
 
A custom version can be built using Gradle following the direction below.

* Clone or Download this project from Github.
* Open a command windows from the <i>MailNodes</i> directory.
* Build using Gradle: <pre>      gradle vdabPackage</pre>

This builds a package zip file which contains the components that need to be deployed. These can be deployed by 
manually unzipping these files as detailed in the [Server Updates](https://vdabtec.com/vdab/docs/VDABGUIDE_ServerUpdates.pdf) 
 documentation.

### Known Issues as of 24 Oct  2018

* While these nodes are very capable and can handle both text and attachments, it is anticipated
that they not work with every multipart mime combination or email provider.


