/*
Blue Iris Server
SmartThings Custom Device Type Handler for Blue Iris Webserver: http://blueirissoftware.com/
Intended to be installed via and managed from BI Fusion Smartapp (below), but can be run standalone

Copyright 2017 FLYJMZ (flyjmz230@gmail.com)

Shoutout and big thanks to Ken from Blue Iris! His great support made this happen.  Plus Blue Iris is an outstanding camera platform.

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
in compliance with the License. You may obtain a copy of the License at:

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
for the specific language governing permissions and limitations under the License.
*/


//////////////////////////////////////////////////////////////////////////////////////////////
///										App Info											//
//////////////////////////////////////////////////////////////////////////////////////////////
/*
SmartThings Community Thread:  
		https://community.smartthings.com/t/release-blue-iris-device-handler/ 

Github: 
		https://github.com/flyjmz/jmzSmartThings/tree/master/devicetypes/flyjmz/blue-iris-server.src
        
BI Fusion SmartThings Community Thread: 
		https://community.smartthings.com/t/release-bi-fusion-v3-0-adds-blue-iris-device-type-handler-blue-iris-camera-dth-motion-sensing/103032
        
Version History:
1.0     14Oct2017   Initial Commit of Standalone DTH
1.01    14Oct2017   Re-fixed bug I accidentally reintroduced where the network ID doesn't get updated properly
2.0		26Oct17		Now can be installed through the BI Fusion smartapp (preferred method).
					Also added software update notifications, and BI camera integration (via BI Fusion)..
                    Non-destructive install, it can be run standalone or through BI Fusion.
2.1		1Nov17		Added Parse handling for preset movements from camera DTH		                   

To Do:
-Nothing!

Wish List:
-"Commands that take parameters" cannot be called with their parameters from tile action, resulting in a different command for each profile.  Some solutions may exist using child device buttons/switches.
-Label wishes:
--The label can't be centered within the tile (which prevents making 2-line labels (using '/n' or '/r'), because right now the second line just goes below the tile).
--The label's text color can't be specified
--The 'on' status for each tile lets me have the background change but then the label says on instead of the profile's name
*/

def appVersion() {"2.1"}

metadata {
    definition (name: "Blue Iris Server", namespace: "flyjmz", author: "flyjmz230@gmail.com") {
        capability "Refresh"
        capability "Bridge"
        attribute "blueIrisProfile", "Number"
        attribute "stoplight", "enum", ["red", "green", "yellow"]
        attribute "errorMessage", "String"
        attribute "cameraMotionActive", "String" //returns camera shortName
        attribute "cameraMotionInactive", "String" //returns camera shortName
        command "setBlueIrisProfile0"
        command "setBlueIrisProfile1"
        command "setBlueIrisProfile2"
        command "setBlueIrisProfile3"
        command "setBlueIrisProfile4"
        command "setBlueIrisProfile5"
        command "setBlueIrisProfile6"
        command "setBlueIrisProfile7"
        command "syncBIprofileToSmarthThings"
        command "setBlueIrisStopLight"
        command "changeHoldTemp"
        command "initializeServer"
        command "setProfile", ["number"]
       	command "triggerCamera", ["string"]
    }

    simulator {
        // TODO: define status and reply messages here
    }

    tiles(scale: 2) {
        valueTile("blueIrisProfile", "device.blueIrisProfile", width: 4, height: 2, canChangeIcon: false, decoration: "flat") {
            state("default", label: '${currentValue}'/*, icon: "https://raw.githubusercontent.com/flyjmz/jmzSmartThings/master/resources/BlueIris_logo.png"*/)    //todo - can I have the icon displayed on the 'My Home' page but not on the actual device tile?
        }
        standardTile("refresh", "device.refresh", width: 2, height: 1, inactiveLabel: false, decoration: "flat") {
            state("default", label:'', action: "refresh.refresh", icon:"st.secondary.refresh", nextState:"refreshing")
            state("refreshing", label:"Refreshing", backgroundColor: "#00a0dc")
        } 
        standardTile("sync", "device.sync", width: 2, height: 1, decoration: "flat") {
            state("default", label:'Sync BI to ST', action: "syncBIprofileToSmarthThings", nextState:"syncing")
            state("syncing", label:"Syncing", backgroundColor: "#00a0dc")
        } 
        standardTile("profile0", "device.profile0mode", width: 2, height: 1, decoration: "flat") {
            state("default", label:'${currentValue}', action: "setBlueIrisProfile0", backgroundColor: "#ffffff", nextState:"turningOn")
            state("on", label:'${currentValue}', backgroundColor: "#00a0dc")
            state("turningOn", label:"Turning On", backgroundColor: "#00a0dc")
        }
        standardTile("holdTemp", "device.holdTemp", width: 2, height: 1, decoration: "flat") {
            state("Hold", label:'${currentValue}', action: "changeHoldTemp", backgroundColor: "#ffffff", nextState:"changing")
            state("Temporary", label:'${currentValue}', action: "changeHoldTemp", backgroundColor: "#ffffff", nextState:"changing")
            state("changing", label:"Changing...", backgroundColor: "#ffffff")
        }
        standardTile("profile1", "device.profile1mode", width: 2, height: 2, decoration: "flat") {
            state("default", label:'${currentValue}', action: "setBlueIrisProfile1", backgroundColor: "#ffffff", nextState:"turningOn")
            state("on", label:'${currentValue}', backgroundColor: "#00a0dc")
            state("turningOn", label:"Turning On", backgroundColor: "#00a0dc")
        }
        standardTile("profile2", "device.profile2mode", width: 2, height: 2, decoration: "flat") {
            state("default", label:'${currentValue}', action: "setBlueIrisProfile2", backgroundColor: "#ffffff", nextState:"turningOn")
            state("on", label:'${currentValue}', backgroundColor: "#00a0dc")
            state("turningOn", label:"Turning On", backgroundColor: "#00a0dc")
        }
        standardTile("profile3", "device.profile3mode", width: 2, height: 2, decoration: "flat") {
            state("default", label:'${currentValue}', action: "setBlueIrisProfile3", backgroundColor: "#ffffff", nextState:"turningOn")
            state("on", label:'${currentValue}', backgroundColor: "#00a0dc")
            state("turningOn", label:"Turning On", backgroundColor: "#00a0dc")
        }
        standardTile("profile4", "device.profile4mode", width: 2, height: 2, decoration: "flat") {
            state("default", label:'${currentValue}', action: "setBlueIrisProfile4", backgroundColor: "#ffffff", nextState:"turningOn")
            state("on", label:'${currentValue}', backgroundColor: "#00a0dc")
            state("turningOn", label:"Turning On", backgroundColor: "#00a0dc")
        }
        standardTile("profile5", "device.profile5mode", width: 2, height: 2, decoration: "flat") {
            state("default", label:'${currentValue}', action: "setBlueIrisProfile5", backgroundColor: "#ffffff", nextState:"turningOn")
            state("on", label:'${currentValue}', backgroundColor: "#00a0dc")
            state("turningOn", label:"Turning On", backgroundColor: "#00a0dc")
        }
        standardTile("profile6", "device.profile6mode", width: 2, height: 2, decoration: "flat") {
            state("default", label:'${currentValue}', action: "setBlueIrisProfile6", backgroundColor: "#ffffff", nextState:"turningOn")
            state("on", label:'${currentValue}', backgroundColor: "#00a0dc")
            state("turningOn", label:"Turning On", backgroundColor: "#00a0dc")
        }
        standardTile("profile7", "device.profile7mode", width: 2, height: 2, decoration: "flat") {
            state("default", label:'${currentValue}', action: "setBlueIrisProfile7", backgroundColor: "#ffffff", nextState:"turningOn")
            state("on", label:'${currentValue}', backgroundColor: "#00a0dc")
            state("turningOn", label:"Turning On", backgroundColor: "#00a0dc")
        }
        standardTile("stoplight", "device.stoplight", width: 2, height: 2, decoration: "flat") {
            state("red", label:'Traffic Light', action: "setBlueIrisStopLight", backgroundColor: "#bc2323", nextState:"changing")
            state("yellow", label:'Traffic Light', action: "setBlueIrisStopLight", backgroundColor: "#f1d801", nextState:"changing")
            state("green", label:'Traffic Light', action: "setBlueIrisStopLight", backgroundColor: "#44b621", nextState:"changing")
            state("changing", label:'Changing...', backgroundColor: "#ffffff")
        }
        main ('blueIrisProfile')
        details('blueIrisProfile','refresh','sync','profile0','profile1','profile2','holdTemp','profile3','profile4','profile5','profile6','profile7','stoplight')
    }

    preferences {
        section("Blue Iris Server Login Settings:"){  //NOTE: Device Type Handler Preferences can't be a dynamic page, can't do notifications, and can't have paragraph's
            //paragraph "This only functions on a Local Connection to the Blue Iris Server (i.e. LAN, the SmartThings hub is on the same network as the BI Server)?"
            //paragraph "Ensure 'Secure Only' is not checked in Blue Iris' Webserver settings."
            input "host", "text", title: "BI Webserver IP", description: "Must be a local connection", required:true, displayDuringSetup: true
            input "port", "number", title: "BI Webserver Port (e.g. 81)", required:true, displayDuringSetup: true
            input "username", "text", title: "BI Username", description: "Must be an Admin User", required: true, displayDuringSetup: true
            input "password", "password", title: "BI Password", description: "Don't check 'Secure Only' in BI", required: true, displayDuringSetup: true
        }
        section("Blue Iris Profile <=> SmartThings Mode Matching:"){
            //paragraph "Enter the SmartThings Mode Name for the Matching Blue Iris Profiles (Inactive, and #1-7). Be sure to enter it exactly. To ignore a profile number, leave it blank."
            input "profile0", "text", title: "ST Mode for BI Inactive (Profile 0)", description: "Default: Inactive", required:false, displayDuringSetup: true
            input "profile1", "text", title: "ST Mode for BI Profile #1", description: "Leave blank to Ignore", required:false, displayDuringSetup: true
            input "profile2", "text", title: "ST Mode for BI Profile #2", description: "Leave blank to Ignore", required:false, displayDuringSetup: true
            input "profile3", "text", title: "ST Mode for BI Profile #3", description: "Leave blank to Ignore", required:false, displayDuringSetup: true
            input "profile4", "text", title: "ST Mode for BI Profile #4", description: "Leave blank to Ignore", required:false, displayDuringSetup: true
            input "profile5", "text", title: "ST Mode for BI Profile #5", description: "Leave blank to Ignore", required:false, displayDuringSetup: true
            input "profile6", "text", title: "ST Mode for BI Profile #6", description: "Leave blank to Ignore", required:false, displayDuringSetup: true
            input "profile7", "text", title: "ST Mode for BI Profile #7", description: "Leave blank to Ignore", required:false, displayDuringSetup: true
        }
        section("Blue Iris Server Health Monitor"){
            double waitThreshold = 5
            input "waitThreshold", "number", title: "Enter how many seconds to wait after the server request is made before deaming it offline:", description: "Default: 5 sec", required:false, displayDuringSetup: true 
        }
        section("Debug"){
            //paragraph "You can turn on debug logging, viewed in Live Logging on the API website."
            def loggingOn = false
            input "loggingOn", "bool", title: "Debug Logging?"
        }
    }
}

def updated() {
//doesn't do anything because it won't correctly call updateDeviceNetworkId() and because the refresh button keeps call it even though I don't have it coded.  User has to hit refresh anyway, might as well control when this is happening.
}

def updateNetworkID() {  //sets the actual network ID so SmartThings knows what to listen to for Parse()
    if (state.debugLogging) log.debug "updateNetworkID() called, state.correctdeviceNetworkId is ${state.correctdeviceNetworkId}, state.host is ${state.host} and state.port is ${state.port}"
    if (state.correctdeviceNetworkId != null) {
    	if (state.debugLogging) log.debug "updating network id from BI Fusion settings"
        device.deviceNetworkId = state.correctdeviceNetworkId
    } else if (state.host != null && state.port != null) {
    	if (state.debugLogging) log.debug "updating network id from Blue Iris Server Device settings"  
        def hosthex = convertIPtoHex(state.host).toUpperCase()  //Note: it needs to be set to uppercase for the new deviceNetworkId to work in SmartThings
        def porthex = convertPortToHex(state.port).toUpperCase()
        device.deviceNetworkId = "$hosthex:$porthex"
    } else log.error "Error 20: No IP, Port, or deviceNetworkId exist to update"
}

def initialize() {  
	if (state.updatedFromBIFusion) {
        log.error "Error 0: The Blue Iris Server device was installed from BI Fusion, edit it's settings from BI Fusion, not device settings."
        sendEvent(name: "errorMessage", value: "Error! The Blue Iris Server device was installed from BI Fusion, edit it's settings from BI Fusion, not device settings.", descriptionText: "Error! The Blue Iris Server device was installed from BI Fusion, edit it's settings from BI Fusion, not device settings.", displayed: true)
    } else {
    	unschedule()
        state.debugLogging = (loggingOn != null) ? loggingOn : false
        state.serverResponseThreshold = (waitThreshold != null) ? waitThreshold : 5
        log.info "initialize() called, debug logging is ${state.debugLogging}, serverResponseThreshold is ${state.serverResponseThreshold}"

        //initialize variables and set the profile names to the tiles
        if (state.holdChange == null) {
            state.holdChange = true
            sendEvent(name: "holdTemp", value: "Hold", displayed: false)
        } else if (state.holdChange) {sendEvent(name: "holdTemp", value: "Hold", displayed: false)}
        else if (state.holdChange == false) {sendEvent(name: "holdTemp", value: "Temporary", displayed: false)}
        state.host = host
        state.port = port
        state.username = username
        state.password = password
        state.hubCommandReceivedTime = now()
        state.hubCommandSentTime = now()
        state.hubOnline = true
        state.profile0mode = (profile0 != null) ? profile0 : "Inactive"
        state.profile1mode = (profile1 != null) ? profile1 : "Profile 1"
        state.profile2mode = (profile2 != null) ? profile2 : "Profile 2"
        state.profile3mode = (profile3 != null) ? profile3 : "Profile 3"
        state.profile4mode = (profile4 != null) ? profile4 : "Profile 4"
        state.profile5mode = (profile5 != null) ? profile5 : "Profile 5"
        state.profile6mode = (profile6 != null) ? profile6 : "Profile 6"
        state.profile7mode = (profile7 != null) ? profile7 : "Profile 7"
		setTileProfileModesToName()
        if(state.debugLogging) log.debug "profile0mode is ${state.profile0mode}, profile1mode is ${state.profile1mode}, profile2mode is ${state.profile2mode}, profile3mode is ${state.profile3mode}, profile4mode is ${state.profile4mode}, profile5mode is ${state.profile5mode}, profile6mode is ${state.profile6mode}, profile7mode is ${state.profile7mode}"
        runEvery15Minutes(retrieveCurrentStatus)
        schedule(now(), checkForUpdates)
        checkForUpdates()
    }
}

def initializeServer(blueIrisServerSettings) {  //The same as initialize(), but run from the data pushed to the device from the Service Manager:
	state.blueIrisServerSettings = blueIrisServerSettings
    unschedule()
    state.updatedFromBIFusion = true  //prevents the other initialize from setting all of this from the Device Preferences Settings
    state.debugLogging = (state.blueIrisServerSettings.loggingOn != null) ? state.blueIrisServerSettings.loggingOn : false 
    if(state.debugLogging) log.debug "initializeServer() received these settings from BI Fusion: ${state.blueIrisServerSettings}"
    state.serverResponseThreshold = (state.blueIrisServerSettings.waitThreshold != null) ? state.blueIrisServerSettings.waitThreshold : 5
    state.holdChange = state.blueIrisServerSettings.holdTemp
    if (state.holdChange == null) {
        state.holdChange = true
        sendEvent(name: "holdTemp", value: "Hold", displayed: false)
    } else if (state.holdChange) {sendEvent(name: "holdTemp", value: "Hold", displayed: false)}
    else if (state.holdChange == false) {sendEvent(name: "holdTemp", value: "Temporary", displayed: false)}
    log.info "initializeServer() called, debug logging is ${state.debugLogging}, serverResponseThreshold is ${state.serverResponseThreshold}"
    state.host = state.blueIrisServerSettings.host
    state.port = state.blueIrisServerSettings.port
    state.username =  state.blueIrisServerSettings.username
    state.password = state.blueIrisServerSettings.password
    state.correctdeviceNetworkId = state.blueIrisServerSettings.DNI
    state.hubCommandReceivedTime = now()
    state.hubCommandSentTime = now()
    state.hubOnline = true
    def profileModeMap = state.blueIrisServerSettings.profileModeMap
    state.profile0mode = profileModeMap[0].modeName
    state.profile1mode = profileModeMap[1].modeName
    state.profile2mode = profileModeMap[2].modeName
    state.profile3mode = profileModeMap[3].modeName
    state.profile4mode = profileModeMap[4].modeName
    state.profile5mode = profileModeMap[5].modeName
    state.profile6mode = profileModeMap[6].modeName
    state.profile7mode = profileModeMap[7].modeName
	if(state.debugLogging) log.debug "state.correctdeviceNetworkId is ${state.correctdeviceNetworkId}"
	setTileProfileModesToName()
    if(state.debugLogging) log.debug "profile0mode is ${state.profile0mode}, profile1mode is ${state.profile1mode}, profile2mode is ${state.profile2mode}, profile3mode is ${state.profile3mode}, profile4mode is ${state.profile4mode}, profile5mode is ${state.profile5mode}, profile6mode is ${state.profile6mode}, profile7mode is ${state.profile7mode}"
    runEvery15Minutes(retrieveCurrentStatus)
}

def parse(description) {
    if(state.debugLogging) log.debug "Parsing got event"   
    state.hubCommandReceivedTime = now()
    
    //Parse Blue Iris Server Response to command from SmartThings
    def msg = parseLanMessage(description)
    def headersAsString = msg.header // => headers as a string
    def headerMap = msg.headers      // => headers as a Map
    def body = msg.body              // => request body as a string
    def status = msg.status          // => http status code of the response
    if (state.debugLogging) {
        log.debug "msg: ${msg}"
        log.debug "headersAsString: ${headersAsString}"
        log.debug "headerMap: ${headerMap}"
        log.debug "body: ${body}"
        log.debug "status: ${status}"
    }
    if (msg) {   //was getting some parse(null) from updated() running when installed from BI Fusion, this fixes that
        parseStatus(status)
        parseBody(body) 
    }
}

def parseBody(body) {
    try {
    	def presetCommandTest = body.split()
        if (presetCommandTest.size() <= 1) {  //First determine if it came from a preset movement command
            log.info "Camera Moved to Preset Successfully" //todo- Can I determine which camera? Pass this as a notification or device status?
        
        //If not preset, then it's a traffic light/profile/trigger response:
        } else { 
            //First separate out if a camera was triggered or not
            def bodyArrayForTrigger = body.split('=')  //turns "signal=green profile=3 camera=Porch" into "signal,green profile,3 camera,Porch" //Using just split() would split any two-word camera names into two separate things.
            if (bodyArrayForTrigger.size() > 3) {parseTrigger(bodyArrayForTrigger[3])}	//we know we have a triggered camera

            //Then parse out traffic signal and profile
            def bodyArray = body.split()
            def newSignal = bodyArray[0] - "signal="
            def newProfile = bodyArray[1] - "profile="
            def newProfileNum = newProfile.toInteger()
            def newProfileName = getprofileName(newProfileNum)
            log.info "parsing results: profile is number '$newProfileNum', named '$newProfileName', signal is '$newSignal'"

            //Then update Tiles
            sendEvent(name: "blueIrisProfile", value: "${newProfileName}", descriptionText: "Blue Iris Profile is ${newProfileName}", displayed: true)
            if (!state.hubOnline) {  	//Sends a notification that it is back online since we had previously said it was offline
                log.info "BI Server is back online"
                sendEvent(name: "errorMessage", value: "Blue Iris Server is back online, Profile is '${newProfileName}' and Traffic Light is '${newSignal}.'", descriptionText: "Blue Iris Server is back online, Profile is '${newProfileName}' and Traffic Light is '${newSignal}.'", displayed: true)
                state.hubOnline = true
            }
            sendEvent(name: "stoplight", value: "$newSignal", descriptionText: "Blue Iris Traffic Signal is ${newSignal}", displayed: true)
            sendEvent(name: "sync", value: "default", displayed: false)
            sendEvent(name: "refresh", value: "default", displayed: false)
            if (newProfileNum ==0) sendEvent(name: "profile0mode", value: "on", displayed: false)
            if (newProfileNum ==1) sendEvent(name: "profile1mode", value: "on", displayed: false)
            if (newProfileNum ==2) sendEvent(name: "profile2mode", value: "on", displayed: false)
            if (newProfileNum ==3) sendEvent(name: "profile3mode", value: "on", displayed: false)
            if (newProfileNum ==4) sendEvent(name: "profile4mode", value: "on", displayed: false)
            if (newProfileNum ==5) sendEvent(name: "profile5mode", value: "on", displayed: false)
            if (newProfileNum ==6) sendEvent(name: "profile6mode", value: "on", displayed: false)
            if (newProfileNum ==7) sendEvent(name: "profile7mode", value: "on", displayed: false)
            if (newProfileNum !=0) sendEvent(name: "profile0mode", value: "${state.profile0mode}", displayed: false)
            if (newProfileNum !=1) sendEvent(name: "profile1mode", value: "${state.profile1mode}", displayed: false)
            if (newProfileNum !=2) sendEvent(name: "profile2mode", value: "${state.profile2mode}", displayed: false)
            if (newProfileNum !=3) sendEvent(name: "profile3mode", value: "${state.profile3mode}", displayed: false)
            if (newProfileNum !=4) sendEvent(name: "profile4mode", value: "${state.profile4mode}", displayed: false)
            if (newProfileNum !=5) sendEvent(name: "profile5mode", value: "${state.profile5mode}", displayed: false)
            if (newProfileNum !=6) sendEvent(name: "profile6mode", value: "${state.profile6mode}", displayed: false)
            if (newProfileNum !=7) sendEvent(name: "profile7mode", value: "${state.profile7mode}", displayed: false)

            //Finally, notify if there are errors
            if(state?.desiredNewProfile && state?.desiredNewProfile != newProfileName) {
                log.error "error 1: ${state.desiredNewProfile} != ${newProfileName}"
                sendEvent(name: "errorMessage", value: "Error! Blue Iris Profile failed to change to ${state.desiredNewProfile}; it is ${newProfileName}", descriptionText: "Error! Blue Iris Profile failed to change to ${state.desiredNewProfile}; it is ${newProfileName}", displayed: true)
            }
            if(state?.desiredNewStoplightColor && state?.desiredNewStoplightColor != newSignal) {
                if(state.desiredNewStoplightColor == 'yellow' && newSignal == 'green'){
                    //Do nothing, not a problem, if the user has Blue Iris to skip yellow, or the yellow delay is a very short time, it'll end up on green
                } else {
                    log.error "error 2: ${state.desiredNewStoplightColor} != ${newSignal}"
                    sendEvent(name: "errorMessage", value: "Error! Blue Iris Traffic Light failed to change to ${state.desiredNewStoplightColor}; it is ${newSignal}", descriptionText: "Error! Blue Iris Traffic Light failed to change to ${state.desiredNewStoplightColor}; it is ${newSignal}", displayed: true)
                }
            }
        }
    } catch (Exception e) {
        log.error "error 21: Parsing parseBody() error, body is '$body'.  Error: $e"
        setTileProfileModesToName()
    }
    /////////////////////Testing Block////////////////////
    //log.debug "11 ${device.currentState("blueIrisProfile").value}"  //returns attribute value, eg Away
    //log.debug "12 ${device.currentState("blueIrisProfile").name}" //returns attribute name, eg blueIrisProfile
    //////////////////////////////////////////////////////
}

def parseStatus(status) {
    try {
        if(status.toInteger() != 200){   //200 is ok, 100-300 series should be ok usually, 400 & 500 series are definitely problems
            if (status.toInteger() >= 400){
                log.error "error 3: msg.status returned ${status.toInteger()}"
                sendEvent(name: "errorMessage", value: "Error! Blue Iris Server returned http status code ${status.toInteger()}, an error.", descriptionText: "Error! Blue Iris Server returned http status code ${status.toInteger()}, an error.", displayed: true)
            } else {
                log.error "error 4: msg.status returned ${status.toInteger()}"
                sendEvent(name: "errorMessage", value: "Error! Blue Iris Server returned http status code ${status.toInteger()}, which may indicate an error.", descriptionText: "Error! Blue Iris Server returned http status code ${status.toInteger()}, which may indicate an error.", displayed: true)
            }
        }  //else status is 200, ok!
    } catch (Exception e) {
        log.error "error 22: Parsing parseStatus() error, status is '$status'.  Error: $e"
    }
}

def parseTrigger(triggeredCameraName) { 
    def shortName = triggeredCameraName.split()
    try {
        log.info "parsing results: triggered Camera short name is '${shortName[0]}'"
        //This was part of trying to not use OAuth.  Todo - make it work
        //sendEvent(name: "cameraMotionActive", value: "${shortName[0]}", displayed: false)  
        //sendEvent(name: "cameraMotionInactive", value: "${shortName[0]}", displayed: false)
    } catch (Exception e) {
        log.error "error 23: Parsing parseTrigger() error, triggeredCameraName is '$shortName[0]'.  Error: $e"
    }
}

def setTileProfileModesToName() {
    sendEvent(name: "profile0mode", value: "${state.profile0mode}", displayed: false)
    sendEvent(name: "profile1mode", value: "${state.profile1mode}", displayed: false)
    sendEvent(name: "profile2mode", value: "${state.profile2mode}", displayed: false)
    sendEvent(name: "profile3mode", value: "${state.profile3mode}", displayed: false)
    sendEvent(name: "profile4mode", value: "${state.profile4mode}", displayed: false)
    sendEvent(name: "profile5mode", value: "${state.profile5mode}", displayed: false)
    sendEvent(name: "profile6mode", value: "${state.profile6mode}", displayed: false)
    sendEvent(name: "profile7mode", value: "${state.profile7mode}", displayed: false)
}

def changeHoldTemp() {
    if(state.debugLogging) log.debug "changeHoldTemp() called, state.holdChange is ${state.holdChange}"
    if(state.holdChange) {
        state.holdChange = false
        sendEvent(name: "holdTemp", value: "Temporary", displayed: false)
    }
    else {
        state.holdChange = true
        sendEvent(name: "holdTemp", value: "Hold", displayed: false)
    }
    if(state.debugLogging) log.debug "changeHoldTemp() done, state.holdChange is now ${state.holdChange}"
}

def customPolling() {
    double timesSinceContact = (now() - state.hubCommandReceivedTime).abs() / 60000  //time since last contact from server in minutes
    if (timesSinceContact > 15) {
        retrieveCurrentStatus()		//Only does a check ('poll') if we haven't heard from the server in more than 15 mintues.
    }    
}

def retrieveCurrentStatus() {
    log.info "Executing 'retrieveCurrentStatus()'"
    def retrieveProfileCommand = "/admin&user=${state.username}&pw=${state.password}"
    hubTalksToBI(retrieveProfileCommand)
}

def refresh() {
    if (state.debugLogging) log.debug "Executing 'refresh'"
    if (state.updatedFromBIFusion) initializeServer(state.blueIrisServerSettings)
    else initialize()
    updateNetworkID()
    retrieveCurrentStatus()
}

def setBlueIrisStopLight() {
    if(state.debugLogging) log.debug "Executing 'setBlueIrisStopLight' with stoplight currently ${device.currentState("stoplight").value}"  
    //Blue Iris http command "/admin?signal=x" Changes the traffic signal state and returns the current state.  
    //x=0 for red (not recording), x=1 for green (recording, x=2 for yellow (pause before starting to record).  This requires admin authentication.   
    def stopligthNow = device.currentState("stoplight").value
    def newStoplight = 2
    if(stopligthNow == 'red') {
        newStoplight = 2
        state.desiredNewStoplightColor = 'yellow'
    } else if(stopligthNow == 'yellow'){
        newStoplight = 1
        state.desiredNewStoplightColor = 'green'
    } else if(stopligthNow == 'green'){
        newStoplight = 0
        state.desiredNewStoplightColor = 'red'
    }
    def changeStopLightCommand = "/admin?signal=${newStoplight}&user=${state.username}&pw=${state.password}"
    hubTalksToBI(changeStopLightCommand)
    log.info "Changing stoplight to '${state.desiredNewStoplightColor}'"
}

def syncBIprofileToSmarthThings() {
    def smartThingsMode = location.currentMode.name
    def profileToSet = getprofileNumber(smartThingsMode)
    if (state.debugLogging) log.debug "Executing 'syncBIprofileToSmarthThings', mode is '${location.mode}', sending profile '$profileToSet'"
    setProfile(profileToSet)
}

def setBlueIrisProfile0() {
    if(state.debugLogging) log.debug "Executing 'setBlueIrisProfile0'"
    setProfile(0)
}

def setBlueIrisProfile1() {
    if(state.debugLogging) log.debug "Executing 'setBlueIrisProfile1'"
    setProfile(1)
}

def setBlueIrisProfile2() {
    if(state.debugLogging) log.debug "Executing 'setBlueIrisProfile2'"
    setProfile(2)
}

def setBlueIrisProfile3() {
    if(state.debugLogging) log.debug "Executing 'setBlueIrisProfile3'"
    setProfile(3)
}

def setBlueIrisProfile4() {
    if(state.debugLogging) log.debug "Executing 'setBlueIrisProfile4'"
    setProfile(4)
}

def setBlueIrisProfile5() {
    if(state.debugLogging) log.debug "Executing 'setBlueIrisProfile5'"
    setProfile(5)
}

def setBlueIrisProfile6() {
    if(state.debugLogging) log.debug "Executing 'setBlueIrisProfile6'"
    setProfile(6)
}

def setBlueIrisProfile7() {
    if(state.debugLogging) log.debug "Executing 'setBlueIrisProfile7'"
    setProfile(7)
}

def setProfile(profile) {
    if (state.debugLogging) "setprofile() received $profile"
    def name = getprofileName(profile)
    log.info "Changing Blue Iris Profile to '${profile}', named '${name}'"
    state.desiredNewProfile = name
    //Blue Iris Param "&lock=0/1/2" makes profile changes as: run/temp/hold
    def lock = 1
    if (state.holdChange) {lock = 2}
    def changeProfileCommand = "/admin?profile=${profile}&lock=${lock}&user=${state.username}&pw=${state.password}"
    hubTalksToBI(changeProfileCommand)
}

def hubTalksToBI(command) {
    state.hubCommandSentTime = now()
    def biHost = "${state.host}:${state.port}"       //NOTE: For device type handlers, the host has to be the ip:port in lowercase hex, but in smartapps the host has to be either a web address or ip:port in normal decimal format (192.168....)
    def sendHTTP = new physicalgraph.device.HubAction(
        method: "GET",
        path: command,
        headers:    [
            HOST:       biHost,
            Accept:     "*/*",
        ]
    )
    if (state.debugLogging) log.debug sendHTTP
    runIn(state.serverResponseThreshold, serverOfflineChecker)
    sendHubCommand(sendHTTP)
}

def serverOfflineChecker() {
    if (state.debugLogging) log.debug "serverOfflineChecker() has state.hubCommandReceivedTime at ${state.hubCommandReceivedTime} and state.hubCommandSentTime at ${state?.hubCommandSentTime}"
    if (state.hubCommandReceivedTime && state.hubCommandSentTime) {
        double responseTime = (state.hubCommandSentTime - state.hubCommandReceivedTime).abs() / 1000  //response time in seconds
        if (state.debugLogging) log.debug "serverOfflineChecker() found server response time was ${responseTime}"
        if (responseTime > state.serverResponseThreshold) {
            log.error "error 9: Server Response time was ${responseTime} seconds, the server is offline."
            sendEvent(name: "errorMessage", value: "Error! Blue Iris Server is offline!", descriptionText: "Error! Blue Iris Server is offline!", displayed: true)  //It has to be the BI server or the SmartThing hub's connection to the BI Server (because otherwise you're hub would be offline too and the SmartThings app would tell you.)
            sendEvent(name: "blueIrisProfile", value: "${getprofileName(8)}", descriptionText: "Blue Iris Profile is ${getprofileName(8)}", displayed: true)
            setTileProfileModesToName()
            state.hubOnline = false
        } else state.hubOnline = true       
    }
    if (!state.hubCommandReceivedTime) {  //shouldn't ever have a null received time because it is a state value setup in initialize()
        log.error "error 9.1: Server doesn't have a received time, so it is offline. (doesn't have one because it's the first health check and it is offline)"
        sendEvent(name: "errorMessage", value: "Error! Blue Iris Server is offline!", descriptionText: "Error! Blue Iris Server is offline!", displayed: true)  //It has to be the BI server or the SmartThing hub's connection to the BI Server (because otherwise you're hub would be offline too and the SmartThings app would tell you.)
		sendEvent(name: "blueIrisProfile", value: "${getprofileName(8)}", descriptionText: "Blue Iris Profile is ${getprofileName(8)}", displayed: true)
		state.hubOnline = false
    }
    if (!state.hubCommandSentTime || ((state.hubCommandSentTime - now()).abs() / 1000) > (state.serverResponseThreshold + 10)) {  //checks to make sure the hubCommandSetTime is from the actual command we intend to compare against (it will always have a value, but if it didn't run correctly the value will be from a previous execution)
        log.error "error 9.2: Server doesn't have a sent time, SmartThings never sent a command, check settings and hub."
        sendEvent(name: "errorMessage", value: "Error! Either SmartThings Hub or Blue Iris Server is offline!", descriptionText: "Either SmartThings Hub or Blue Iris Server is offline!", displayed: true)  //It has to be the BI server or the SmartThing hub's connection to the BI Server (because otherwise you're hub would be offline too and the SmartThings app would tell you.)
    	state.hubOnline = false
    }
}

def getprofileName(number) {
    if (state.debugLogging) log.debug "getprofileName got ${number}"
    def name = 'Away'
    if (number == 0) {name = state.profile0mode}
    else if (number == 1) {name = state.profile1mode}
    else if (number == 2) {name = state.profile2mode}
    else if (number == 3) {name = state.profile3mode}
    else if (number == 4) {name = state.profile4mode}
    else if (number == 5) {name = state.profile5mode}
    else if (number == 6) {name = state.profile6mode}
    else if (number == 7) {name = state.profile7mode}
    else if (number == 8) {name = "OFFLINE!"}
    else {
        log.error "error 10: getprofileName(number) got a profile number outside of the 0-7 range, check the settings of what you passed it."
        sendEvent(name: "errorMessage", value: "Error! A Blue Iris Profile number was passed outside of the 0-7 range. Check settings.", descriptionText: "Error! A Blue Iris Profile number was passed outside of the 0-7 range. Check settings.", displayed: true)
    }
    if (state.debugLogging) log.debug "getprofileName returning ${name}"
    return name
}

def getprofileNumber(name) {
    if (state.debugLogging) log.debug "getprofileNumber got ${name}"
    def number = 1
    if (name == state.profile0mode) {number = 0}
    else if (name == state.profile1mode) {number = 1}
    else if (name == state.profile2mode) {number = 2}
    else if (name == state.profile3mode) {number = 3}
    else if (name == state.profile4mode) {number = 4}
    else if (name == state.profile5mode) {number = 5}
    else if (name == state.profile6mode) {number = 6}
    else if (name == state.profile7mode) {number = 7}
    else if (name == "OFFLINE!") {number = 8}
    else {
        log.error "error 11: getprofileNumber(name) got a name that isn't one of the user defined profiles, check profile name settings"
        sendEvent(name: "errorMessage", value: "Error! A Blue Iris Profile name was passed that isn't one of the user defined profiles. Check settings.", descriptionText: "Error! A Blue Iris profile name was passed that isn't one of the user defined profiles. Check settings.", displayed: true)
    }
    if (state.debugLogging) log.debug "getprofileNumber returning ${number}"
    return number
}

private String convertIPtoHex(ipAddress) {
    try {
        String hex = ipAddress.tokenize('.').collect {String.format('%02x', it.toInteger())}.join()
        return hex
    } catch (Exception e) {
        log.error "error 12: Invalid IP Address $ipAddress, check settings. Error: $e"
        sendEvent(name: "errorMessage", value: "Invalid Blue Iris Server IP Address $ipAddress, check settings", descriptionText: "Invalid Blue Iris Server IP Address $ipAddress, check settings", displayed: true)
    }
}

private String convertPortToHex(port) {
    if (!port || (port == 0)) {
        log.error "error 13: Invalid port $port, check settings."
        sendEvent(name: "errorMessage", value: "Invalid Blue Iris Server Port $port, check settings", descriptionText: "Invalid Blue Iris Server port $port, check settings", displayed: true)
    }

    try {
        String hexport = port.toString().format('%04x', port.toInteger())
        return hexport
    } catch (Exception e) {
        log.error "error 14: Invalid port $port, check settings. Error: $e"
        sendEvent(name: "errorMessage", value: "Invalid Blue Iris Server Port $port, check settings", descriptionText: "Invalid Blue Iris Server port $port, check settings", displayed: true)
    }
}

def checkForUpdates() {  	//max version size it can check is 4 levels, e.g. version 1.2.3.4
    log.info "Checking for software updates"
    def publishedVersion = "0.0"
    try {
        httpGet([uri: "https://raw.githubusercontent.com/flyjmz/jmzSmartThings/master/devicetypes/flyjmz/blue-iris-server.src/version.txt", contentType: "text/plain; charset=UTF-8"]) { resp ->
            if(resp.data) {
                publishedVersion= resp?.data?.text.toString()   //For some reason I couldn't add .trim() to this line, or make another variable and add it there.  The rest of the code would run, but notifications failed. Just having .trim() in the notification below failed a bunch, then started working again...
                return publishedVersion
            } else  log.error "retrievePublishedVersion() response from httpGet was ${resp}"
        }
    }
    catch (Exception e) {
        log.error "checkForUpdates() couldn't get the current code verison. Error:$e"
        publishedVersion = "0.0"
    }
    def installedVersion = appVersion()
    if (state.debugLogging) log.debug "publishedVersion from web is ${publishedVersion}, installedVersion is ${installedVersion}"
    def instVerNum = 0			    
    def webVerNum = 0
    if (publishedVersion && installedVersion) {  //make sure no null
        def instVerMap = installedVersion.tokenize('.')  //makes a map of each level of the version
        def webVerMap = publishedVersion.tokenize('.')
        def instVerMapSize = instVerMap.size()
        def webVerMapSize = webVerMap.size()
        if (instVerMapSize > webVerMapSize) {   //handles mismatched sizes (e.g. they have v1.3 installed but v2 is out and didn't write it as v2.0)
            def sizeMismatch = instVerMapSize - webVerMapSize
            for (int i = 0; i < sizeMismatch; i++) {
                def newMapPosition = webVerMapSize + i  //maps' first postion is [0], but size would count it, so the next map position is i in the loop because i starts with 0
                webVerMap[newMapPosition] = 0  //just make it a zero, the actual goal is to increase the size of the map
            }
        } else if (instVerMapSize < webVerMapSize) {
            def sizeMismatch = webVerMapSize - instVerMapSize
            for (int i = 0; i < sizeMismatch; i++) {
                def newMapPosition = instVerMapSize + i  
                instVerMap[newMapPosition] = 0
            }
        }
        instVerMapSize = instVerMap.size() //update the sizes incase we just changed the maps
        webVerMapSize = webVerMap.size()
        if (state.debugLogging) log.debug "instVerMapSize is $instVerMapSize and the map is $instVerMap and webVerMapSize is $webVerMapSize and the map is $webVerMap"
        for (int i = 0; i < instVerMapSize; i++) {
            instVerNum = instVerNum + instVerMap[i]?.toInteger() *  Math.pow(10, (instVerMapSize - i))  //turns the version segments into one big additive number: 1.2.3 becomes 100+20+3 = 123
        }
        for (int i = 0; i < webVerMapSize; i++) {
            webVerNum = webVerNum + webVerMap[i]?.toInteger() * Math.pow(10, (webVerMapSize - i)) 
        }
        if (state.debugLogging) log.debug "publishedVersion is now ${webVerNum}, installedVersion is now ${instVerNum}"
        if (webVerNum > instVerNum) {
            def msg = "Blue Iris Server Device Type Handler Update Available. Update in IDE. v${installedVersion} installed, v${publishedVersion.trim()} available."
            sendEvent(name: "errorMessage", value: "$msg", descriptionText: "$msg", displayed: true)
        } else if (webVerNum == instVerNum) {
            log.info "Blue Iris Server DTH is current."
        } else if (webVerNum < instVerNum) {
            log.error "Your installed version of the Blue Iris Server DTH seems to be higher than the published version."
        }
    } else if (!publishedVersion) {log.error "Cannot get published app version from the web."}
}

def triggerCamera(cameraShortName) {
    log.info "triggering '$cameraShortName'"
    def triggerCameraCommand = "/admin?camera=${cameraShortName}&trigger&user=${state.username}&pw=${state.password}"
    hubTalksToBI(triggerCameraCommand)
}
