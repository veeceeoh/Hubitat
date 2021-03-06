/**
 * Custom WU Driver
 *
 *  Copyright 2018 mattw01
 *
 *  This driver was originally written by @mattw01 and I thank him for that!
 *  Heavily modified by myself: @Cobra with lots of help from @Scottma61 ( @Matthew )
 *  and with valuable input from the Hubitat community
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *  Last Update 03/05/2018
 *
 *  V2.4.1 - Debug - Changed the switchable capabilities to allow them to be seen by 'rule machine'- @Cobra 03/05/2018
 *  V2.4.0 - Added switchable 'Capabilities & Lowercase Data' for use with dashboards & Rule Machine - @Cobra 02/05/2018
 *  V2.3.0 - Added Moon phase and illumination percentage - @Cobra 01/05/2018
 *  V2.2.0 - Added 'Sunrise' and 'Sunset' - Thanks to: @Scottma61 for this one - @Cobra 01/05/2018
 *  V2.1.1 - Added defaultValue to "pollIntervalLimit" to prevent errors on new installs - @Cobra 01/05/2018
 *  V2.1.0 - Added 3 attributes - Rain tomorrow & the day after and Station_State also added poll counter and reset button @Cobra 01/05/2018
 *  V2.0.1 - Changed to one call to WU for Alerts, Conditions and Forecast - Thanks to: @Scottma61 for this one
 *  V2.0.0 - version alignment with lowercase version - @Cobra 27/04/2018 
 *  V1.9.0 - Added 'Chance_Of_Rain' an an attribute (also added to the summary) - @Cobra 27/04/2018 
 *  V1.8.0 - added 'stateChange' to some of the params that were not updating on poll unless changed - @Cobra 27/04/2018 
 *  V1.7.2 - Debug on lowercase version - updated version number for consistancy - @Cobra 26/04/2018 
 *  V1.7.1 - Debug - @Cobra 26/04/2018 
 *  V1.7.0 - Added 'Weather Summary' as a summary of the data with some English in between @Cobra - 26/04/2018
 *  V1.6.0 - Changed some attribute names - @Cobra - 25/04/2018/
 *  V1.5.0 - Added 'Station ID' so you can confirm you are using correct WU station @Cobra 25/04/2018
 *  V1.4.0 - Added ability to choose 'Pressure', 'Distance/Speed' & 'Precipitation' units & switchable logging- @Cobra 25/04/2018
 *  V1.3.0 - Added wind gust - removed some capabilities and added attributes - @Cobra 24/04/2018
 *  V1.2.0 - Added wind direction - @Cobra 23/04/2018
 *  V1.1.0 - Added ability to choose between "Fahrenheit" and "Celsius" - @Cobra 23/03/2018
 *  V1.0.0 - Original @mattw01 version
 *
 */

metadata {
    definition (name: "Custom WU Driver", namespace: "Cobra", author: "mattw01") {
        capability "Actuator"
        capability "Sensor"
        capability "Temperature Measurement"
        capability "Illuminance Measurement"
        capability "Relative Humidity Measurement"
        
        if(lowerCase == true){
            log.info "Lowercase data: ON"
         }
        if(lowerCase == false){ 
            log.info "Lowercase data: OFF"
        }
    

        
        command "Poll"
        command "ForcePoll"
        command "ResetPollCount"
        
    
    
        attribute "Polls_Since_Reset", "number"
        attribute "Solar_Radiation", "number"
        attribute "Observation_Time", "string"
        attribute "Weather", "string"
        attribute "Temperature_Feels_Like", "number"
        attribute "Precip_Last_Hour", "number"
        attribute "Precip_Today", "number"
        attribute "Wind_Speed", "number"
        attribute "Pressure", "number"
        attribute "Dewpoint", "number"
        attribute "UV", "number"
        attribute "Visibility", "number"
        attribute "Forecast_High", "number"
        attribute "Forecast_Low", "number"
        attribute "Forecast_Conditions", "string"
        attribute "Display_Unit_Temperature", "string"
        attribute "Display_Unit_Distance", "string"
        attribute "Display_Unit_Pressure", "string"
        attribute "Display_Unit_Precipitation", "sting"
        attribute "Display_Summary_Format", "string"
        attribute "Wind_Direction", "string"
        attribute "Wind_Gust", "string"
        attribute "Temperature", "string"
        attribute "Illuminance", "string"
        attribute "Humidity", "string"
        attribute "Alert", "string"
        attribute "Driver_Version", "string"
        attribute "Driver_NameSpace", "string"
        attribute "Station_ID", "string"
        attribute "Weather_Summary", "string"
        attribute "Station_City", "string"
        attribute "Station_State", "string"
        attribute "Chance_Of_Rain", "string"
        attribute "Expected_Rain_Tomorrow", "string"
        attribute "Expected_Rain_Day_After_Tomorrow", "string"
   		attribute "Sunrise", "string"
        attribute "Sunset", "string"
        attribute "Moon_Phase", "string"
        attribute "Moon_Illumination", "string"
 
        // Lowercase attributes
 if(lowerCase == true){
		attribute "localSunrise", "string"
        attribute "localSunset", "string"
        attribute "weather", "string"
        attribute "feelsLike", "number"
        attribute "weatherIcon", "string"
		attribute "city", "string"
        attribute "state", "string"
        attribute "percentPrecip", "string"
   }        
     
        
    }
    preferences() {
        section("Query Inputs"){
            input "apiKey", "text", required: true, title: "API Key"
            input "pollLocation", "text", required: true, title: "ZIP Code or Location"
            input "tempFormat", "enum", required: true, title: "Display Unit - Temperature: Fahrenheit or Celsius",  options: ["Fahrenheit", "Celsius"]
            input "distanceFormat", "enum", required: true, title: "Display Unit - Distance/Speed: Miles or Kilometres",  options: ["Miles (mph)", "Kilometres (kph)"]
            input "pressureFormat", "enum", required: true, title: "Display Unit - Pressure: Inches or Millibar",  options: ["Inches", "Millibar"]
            input "rainFormat", "enum", required: true, title: "Display Unit - Precipitation: Inches or Millimetres",  options: ["Inches", "Millimetres"]
            input "pollIntervalLimit", "number", title: "Poll Interval Limit:", required: true, defaultValue: 1
            input "autoPoll", "bool", required: false, title: "Enable Auto Poll"
            input "pollInterval", "enum", title: "Auto Poll Interval:", required: false, defaultValue: "5 Minutes",
                   options: ["5 Minutes", "10 Minutes", "15 Minutes", "30 Minutes", "1 Hour", "3 Hours"]
            input "logSet", "bool", title: "Log All WU Response Data", required: true, defaultValue: false
            input "lowerCase", "bool", title: "Enable 'Lowercase data", required: true, defaultValue: false
            input "summaryType", "bool", title: "Full Weather Summary", required: true, defaultValue: false
            input "iconType", "bool", title: "Icon: On = Current - Off = Forecast", required: true, defaultValue: false
            input "weatherFormat", "enum", required: true, title: "How to format weather summary",  options: ["Celsius, Miles & MPH", "Fahrenheit, Miles & MPH", "Celsius, Kilometres & KPH"]
        }
    }
}

def updated() {
    log.debug "updated called"
    state.version = "2.4.1"    // ************************* Update as required *************************************
    unschedule()
    state.NumOfPolls = 0
    ForcePoll()
    def pollIntervalCmd = (settings?.pollInterval ?: "5 Minutes").replace(" ", "")
    if(autoPoll)
        "runEvery${pollIntervalCmd}"(pollSchedule)
}

def ResetPollCount(){
state.NumOfPolls = -1
    log.info "Poll counter reset.."
ForcePoll()
}

def pollSchedule()
{
    ForcePoll()
}
              
def parse(String description) {
}

def Poll()
{
    if(now() - state.lastPoll > (pollIntervalLimit * 60000))
        ForcePoll()
    else
        log.debug "Poll called before interval threshold was reached"
}

def ForcePoll()
{
    
    state.NumOfPolls = (state.NumOfPolls) + 1
    log.info " state.NumOfPolls = $state.NumOfPolls" 
   
    log.debug "WU: ForcePoll called"
    def params1 = [
        uri: "http://api.wunderground.com/api/${apiKey}/alerts/astronomy/conditions/forecast/q/${pollLocation}.json"
    ]
    
    try {
        httpGet(params1) { resp1 ->
            resp1.headers.each {
            log.debug "Response1: ${it.name} : ${it.value}"
        }
            if(logSet == true){  
           
            log.debug "params1: ${params1}"
            log.debug "response contentType: ${resp1.contentType}"
 		    log.debug "response data: ${resp1.data}"
            } 
            if(logSet == false){ 
            log.info "Further WU detailed data logging disabled"    
            }    
            
       state.IncludeLowerCase = 'No'
            
            sendEvent(name: "Polls_Since_Reset", value: state.NumOfPolls, isStateChange: true)
             sendEvent(name: "Driver_NameSpace", value: "Cobra", isStateChange: true)
             sendEvent(name: "Driver_Version", value: state.version, isStateChange: true)
             sendEvent(name: "Station_ID", value: resp1.data.current_observation.station_id, isStateChange: true)
             sendEvent(name: "Station_City", value: resp1.data.current_observation.display_location.city, isStateChange: true)
             sendEvent(name: "Chance_Of_Rain", value: resp1.data.forecast.simpleforecast.forecastday[0].pop + "%", isStateChange: true)
            sendEvent(name: "Station_State", value: resp1.data.current_observation.display_location.state, isStateChange: true)
            sendEvent(name: "Sunrise", value: resp1.data.sun_phase.sunrise.hour + ":" + resp1.data.sun_phase.sunrise.minute, isStateChange: true)
        	sendEvent(name: "Sunset", value: resp1.data.sun_phase.sunset.hour + ":" + resp1.data.sun_phase.sunset.minute, isStateChange: true)
   			sendEvent(name: "Moon_Phase", value: resp1.data.moon_phase.phaseofMoon , isStateChange: true)
            sendEvent(name: "Moon_Illumination", value: resp1.data.moon_phase.percentIlluminated  + "%" , isStateChange: true)
            
// lowercase events
            if(lowerCase == true){
                state.IncludeLowerCase = 'Yes'
            sendEvent(name: "weather", value: resp1.data.current_observation.weather, isStateChange: true)
            sendEvent(name: "humidity", value: resp1.data.current_observation.relative_humidity.replaceFirst("%", ""), isStateChange: true)
	    	sendEvent(name: "illuminance", value: resp1.data.current_observation.solarradiation, unit: "lux", isStateChange: true)
	   		sendEvent(name: "city", value: resp1.data.current_observation.display_location.city, isStateChange: true)
            sendEvent(name: "state", value: resp1.data.current_observation.display_location.state, isStateChange: true)
            sendEvent(name: "percentPrecip", value: resp1.data.forecast.simpleforecast.forecastday[0].pop , isStateChange: true)
            sendEvent(name: "localSunrise", value: resp1.data.sun_phase.sunrise.hour + ":" + resp1.data.sun_phase.sunrise.minute, descriptionText: "Sunrise today is at $localSunrise", isStateChange: true)
        	sendEvent(name: "localSunset", value: resp1.data.sun_phase.sunset.hour + ":" + resp1.data.sun_phase.sunset.minute, descriptionText: "Sunset today at is $localSunset", isStateChange: true)
             
            
 // Select Icon
                if(iconType == false){   
                   sendEvent(name: "weatherIcon", value: resp1.data.forecast.simpleforecast.forecastday[0].icon, isStateChange: true)
                }
                if(iconType == true){ 
			       sendEvent(name: "weatherIcon", value: resp1.data.current_observation.icon, isStateChange: true)
                }    
            }
           
           def WeatherSummeryFormat = weatherFormat
            
            if(summaryType == true){
            
            if (WeatherSummeryFormat == "Celsius, Miles & MPH"){
                		 sendEvent(name: "Display_Summary_Format", value: "Celsius, Miles & MPH", isStateChange: true)
                         sendEvent(name: "Weather_Summary", value: "Weather summary for" + " " + resp1.data.current_observation.display_location.city + ", " + resp1.data.current_observation.observation_time+ ". "   
                       + resp1.data.forecast.simpleforecast.forecastday[0].conditions + " with a high of " + resp1.data.forecast.simpleforecast.forecastday[0].high.celsius + " degrees, " + "and a low of " 
                       + resp1.data.forecast.simpleforecast.forecastday[0].low.celsius  + " degrees. " + "Humidity is currently around " + resp1.data.current_observation.relative_humidity + " and temperature is " 
                       + resp1.data.current_observation.temp_c + " degrees. " + " The temperature feels like it's " + resp1.data.current_observation.feelslike_c + " degrees. " + "Wind is from the " + resp1.data.current_observation.wind_dir
                       + " at " + resp1.data.current_observation.wind_mph + " mph" + ", with gusts up to " + resp1.data.current_observation.wind_gust_mph + " mph" + ". Visibility is around " 
                       + resp1.data.current_observation.visibility_mi + " miles" + ". " + "There is a "+resp1.data.forecast.simpleforecast.forecastday[0].pop + "% chance of rain today." , isStateChange: true
                      )  
            }
                
            if (WeatherSummeryFormat == "Fahrenheit, Miles & MPH"){
                 		 sendEvent(name: "Display_Summary_Format", value: "Fahrenheit, Miles & MPH", isStateChange: true)
                         sendEvent(name: "Weather_Summary", value: "Weather summary for" + " " + resp1.data.current_observation.display_location.city + ", " + resp1.data.current_observation.observation_time+ ". "  
                       + resp1.data.forecast.simpleforecast.forecastday[0].conditions + " with a high of " + resp1.data.forecast.simpleforecast.forecastday[0].high.fahrenheit + " degrees, " + "and a low of " 
                       + resp1.data.forecast.simpleforecast.forecastday[0].low.fahrenheit  + " degrees. " + "Humidity is currently around " + resp1.data.current_observation.relative_humidity + " and temperature is " 
                       + resp1.data.current_observation.temp_f + " degrees. " + " The temperature feels like it's " + resp1.data.current_observation.feelslike_f + " degrees. " + "Wind is from the " + resp1.data.current_observation.wind_dir
                       + " at " + resp1.data.current_observation.wind_mph + " mph" + ", with gusts up to: " + resp1.data.current_observation.wind_gust_mph + " mph" + ". Visibility is around " 
                       + resp1.data.current_observation.visibility_mi + " miles" + ". " + "There is a "+resp1.data.forecast.simpleforecast.forecastday[0].pop + "% chance of rain today." , isStateChange: true
                      )  
            }   
                
             if (WeatherSummeryFormat == "Celsius, Kilometres & KPH"){
                 		 sendEvent(name: "Display_Summary_Format", value: "Celsius, Kilometres & KPH", isStateChange: true)
                         sendEvent(name: "Weather_Summary", value: "Weather summary for" + " " + resp1.data.current_observation.display_location.city + ", " + resp1.data.current_observation.observation_time+ ". "  
                       + resp1.data.forecast.simpleforecast.forecastday[0].conditions + " with a high of " + resp1.data.forecast.simpleforecast.forecastday[0].high.celsius + " degrees, " + "and a low of " 
                       + resp1.data.forecast.simpleforecast.forecastday[0].low.celsius  + " degrees. " + "Humidity is currently around " + resp1.data.current_observation.relative_humidity + " and temperature is " 
                       + resp1.data.current_observation.temp_c + " degrees. " + " The temperature feels like it's " + resp1.data.current_observation.feelslike_c + " degrees. " + "Wind is from the " + resp1.data.current_observation.wind_dir
                       + " at " + resp1.data.current_observation.wind_kph + " kph" + ", with gusts up to " + resp1.data.current_observation.wind_gust_kph + " kph" + ". Visibility is around " 
                       + resp1.data.current_observation.visibility_km + " kilometres" + ". " + "There is a "+resp1.data.forecast.simpleforecast.forecastday[0].pop + "% chance of rain today." , isStateChange: true
                      )  
            }
                
                
        }    
            
            
            
            
            
            
            
            if(summaryType == false){
                
             if (WeatherSummeryFormat == "Celsius, Miles & MPH"){
                		 sendEvent(name: "Display_Summary_Format", value: "Celsius, Miles & MPH", isStateChange: true)
                         sendEvent(name: "Weather_Summary", value: resp1.data.forecast.simpleforecast.forecastday[0].conditions + ". " + " Forecast High:" + resp1.data.forecast.simpleforecast.forecastday[0].high.celsius + ", Forecast Low:" 
                       + resp1.data.forecast.simpleforecast.forecastday[0].low.celsius  +  ". Humidity: " + resp1.data.current_observation.relative_humidity + " Temperature: " 
                       + resp1.data.current_observation.temp_c  + ". Wind Direction: " + resp1.data.current_observation.wind_dir + ". Wind Speed: " + resp1.data.current_observation.wind_mph + " mph" 
                       + ", Gust: " + resp1.data.current_observation.wind_gust_mph + " mph. Rain: "  +resp1.data.forecast.simpleforecast.forecastday[0].pop + "%" , isStateChange: true
                      )  
            }
            
            if (WeatherSummeryFormat == "Fahrenheit, Miles & MPH"){
                		 sendEvent(name: "Display_Summary_Format", value: "Fahrenheit, Miles & MPH", isStateChange: true)
                         sendEvent(name: "Weather_Summary", value: resp1.data.forecast.simpleforecast.forecastday[0].conditions + ". " + " Forecast High:" + resp1.data.forecast.simpleforecast.forecastday[0].high.fahrenheit + ", Forecast Low:" 
                       + resp1.data.forecast.simpleforecast.forecastday[0].low.fahrenheit  +  ". Humidity: " + resp1.data.current_observation.relative_humidity + " Temperature: " 
                       + resp1.data.current_observation.temp_f  + ". Wind Direction: " + resp1.data.current_observation.wind_dir + ". Wind Speed: " + resp1.data.current_observation.wind_mph + " mph" 
                       + ", Gust: " + resp1.data.current_observation.wind_gust_mph + " mph. Rain:"  +resp1.data.forecast.simpleforecast.forecastday[0].pop + "%", isStateChange: true
                      )  
            }
            
             if (WeatherSummeryFormat ==  "Celsius, Kilometres & KPH"){
                		 sendEvent(name: "Display_Summary_Format", value:  "Celsius, Kilometres & KPH", isStateChange: true)
                         sendEvent(name: "Weather_Summary", value: resp1.data.forecast.simpleforecast.forecastday[0].conditions + ". " + " Forecast High:" + resp1.data.forecast.simpleforecast.forecastday[0].high.celsius + ", Forecast Low:" 
                       + resp1.data.forecast.simpleforecast.forecastday[0].low.celsius  +  ". Humidity: " + resp1.data.current_observation.relative_humidity + " Temperature: " 
                       + resp1.data.current_observation.temp_c  + ". Wind Direction: " + resp1.data.current_observation.wind_dir + ". Wind Speed: " + resp1.data.current_observation.wind_kph + " kph" 
                       + ", Gust: " + resp1.data.current_observation.wind_gust_kph + " kph. Rain:"  +resp1.data.forecast.simpleforecast.forecastday[0].pop + "%", isStateChange: true
                      )  
            }
            
            }    
            
            
    
            

                
            def illume = (resp1.data.current_observation.solarradiation)
            if(illume){
            	 sendEvent(name: "Illuminance", value: resp1.data.current_observation.solarradiation, unit: "lux", isStateChange: true)
                 sendEvent(name: "Solar_Radiation", value: resp1.data.current_observation.solarradiation, unit: "W", isStateChange: true)
            }
            if(!illume){
                 sendEvent(name: "Illuminance", value: "This station does not send Illumination data", isStateChange: true)
            	 sendEvent(name: "Solar_Radiation", value: "This station does not send Solar Radiation data", isStateChange: true)
            }   
            
            sendEvent(name: "Observation_Time", value: resp1.data.current_observation.observation_time, isStateChange: true)
            sendEvent(name: "Weather", value: resp1.data.current_observation.weather, isStateChange: true)
   //         sendEvent(name: "Wind_String", value: resp1.data.current_observation.wind_string)
            sendEvent(name: "Humidity", value: resp1.data.current_observation.relative_humidity, unit: "%", isStateChange: true)
            sendEvent(name: "UV", value: resp1.data.current_observation.UV, isStateChange: true)
            sendEvent(name: "Forecast_Conditions", value: resp1.data.forecast.simpleforecast.forecastday[0].conditions, isStateChange: true)
            sendEvent(name: "Wind_Direction", value: resp1.data.current_observation.wind_dir, isStateChange: true)
            
            
            if(rainFormat == "Inches"){
            sendEvent(name: "Precip_Last_Hour", value: resp1.data.current_observation.precip_1hr_in, unit: "IN", isStateChange: true)
            sendEvent(name: "Precip_Today", value: resp1.data.current_observation.precip_today_in, unit: "IN", isStateChange: true)
            sendEvent(name: "Expected_Rain_Tomorrow", value: resp1.data.forecast.simpleforecast.forecastday[1].qpf_allday.in, unit: "IN", isStateChange: true)
            sendEvent(name: "Expected_Rain_Day_After_Tomorrow", value: resp1.data.forecast.simpleforecast.forecastday[2].qpf_allday.in, unit: "IN", isStateChange: true)
            sendEvent(name: "Display_Unit_Precipitation", value: "Inches", isStateChange: true)
            }
            if(rainFormat == "Millimetres"){   
            sendEvent(name: "Precip_Today", value: resp1.data.current_observation.precip_today_metric, unit: "MM", isStateChange: true)
            sendEvent(name: "Precip_Last_Hour", value: resp1.data.current_observation.precip_1hr_metric, unit: "MM", isStateChange: true)
            sendEvent(name: "Expected_Rain_Tomorrow", value: resp1.data.forecast.simpleforecast.forecastday[1].qpf_allday.mm, unit: "MM", isStateChange: true)
            sendEvent(name: "Expected_Rain_Day_After_Tomorrow", value: resp1.data.forecast.simpleforecast.forecastday[2].qpf_allday.mm, unit: "MM", isStateChange: true)
            sendEvent(name: "Display_Unit_Precipitation", value: "Millimetres", isStateChange: true)
            }
            
            if(tempFormat == "Celsius"){
            sendEvent(name: "Temperature", value: resp1.data.current_observation.temp_c, unit: "C", isStateChange: true)
            sendEvent(name: "Temperature_Feels_Like", value: resp1.data.current_observation.feelslike_c, unit: "C", isStateChange: true)
            sendEvent(name: "Dewpoint", value: resp1.data.current_observation.dewpoint_c, unit: "C", isStateChange: true)
            sendEvent(name: "Forecast_High", value: resp1.data.forecast.simpleforecast.forecastday[0].high.celsius, unit: "C", isStateChange: true)
            sendEvent(name: "Forecast_Low", value: resp1.data.forecast.simpleforecast.forecastday[0].low.celsius, unit: "C", isStateChange: true)
            sendEvent(name: "Display_Unit_Temperature", value: "Celsius", isStateChange: true)
                
 // lowercase
        if(lowerCase == true){
            sendEvent(name: "feelsLike", value: resp1.data.current_observation.feelslike_c, unit: "C", isStateChange: true)   
            sendEvent(name: "temperature", value: resp1.data.current_observation.temp_c, unit: "C", isStateChange: true)
                 }
            	
        }
           if(tempFormat == "Fahrenheit"){ 
           sendEvent(name: "Temperature", value: resp1.data.current_observation.temp_f, unit: "F", isStateChange: true)
           sendEvent(name: "Temperature_Feels_Like", value: resp1.data.current_observation.feelslike_f, unit: "F", isStateChange: true)
           sendEvent(name: "Dewpoint", value: resp1.data.current_observation.dewpoint_f, unit: "F", isStateChange: true)
           sendEvent(name: "Forecast_High", value: resp1.data.forecast.simpleforecast.forecastday[0].high.fahrenheit, unit: "F", isStateChange: true)
           sendEvent(name: "Forecast_Low", value: resp1.data.forecast.simpleforecast.forecastday[0].low.fahrenheit, unit: "F", isStateChange: true)
           sendEvent(name: "Display_Unit_Temperature", value: "Fahrenheit", isStateChange: true)

// lowercase
         if(lowerCase == true){
           sendEvent(name: "feelsLike", value: resp1.data.current_observation.feelslike_f, unit: "F", isStateChange: true)    
           sendEvent(name: "temperature", value: resp1.data.current_observation.temp_f, unit: "F", isStateChange: true)	
                }          	
           }  
            
          if(distanceFormat == "Miles (mph)"){  
            sendEvent(name: "Visibility", value: resp1.data.current_observation.visibility_mi, unit: "mi", isStateChange: true)
            sendEvent(name: "Wind_Speed", value: resp1.data.current_observation.wind_mph, unit: "MPH", isStateChange: true)
            sendEvent(name: "Wind_Gust", value: resp1.data.current_observation.wind_gust_mph, isStateChange: true) 
            sendEvent(name: "Display_Unit_Distance", value: "Miles (mph)", isStateChange: true)
          }  
            
          if(distanceFormat == "Kilometres (kph)"){
           sendEvent(name: "Visibility", value: resp1.data.current_observation.visibility_km, unit: "km", isStateChange: true)
           sendEvent(name: "Wind_Speed", value: resp1.data.current_observation.wind_kph, unit: "KPH", isStateChange: true)  
           sendEvent(name: "Wind_Gust", value: resp1.data.current_observation.wind_gust_kph, isStateChange: true) 
           sendEvent(name: "Display_Unit_Distance", value: "Kilometres (kph)", isStateChange: true)  
          }
                      
            if(pressureFormat == "Inches"){
                
            sendEvent(name: "Pressure", value: resp1.data.current_observation.pressure_in, unit: "mi", isStateChange: true)
            sendEvent(name: "Display_Unit_Pressure", value: "Inches")  
            }
            
            if(pressureFormat == "Millibar"){
            sendEvent(name: "Pressure", value: resp1.data.current_observation.pressure_mb, unit: "mb", isStateChange: true)
            sendEvent(name: "Display_Unit_Pressure", value: "Millibar", isStateChange: true) 
            }
            
   
             def possAlert = (resp1.data.alerts.description)
               if (possAlert){
               sendEvent(name: "Alert", value: resp1.data.alerts.description, isStateChange: true)  
               }
                if (!possAlert){
               sendEvent(name: "Alert", value: " No current weather alerts for this area")
                }
               
          state.lastPoll = now()     

        } 
        
    } catch (e) {
        log.error "something went wrong: $e"
    }
    
}



def Report(){
  def obvTime = Observation_Time.value
    
  log.info "$obvTime"  
    
}