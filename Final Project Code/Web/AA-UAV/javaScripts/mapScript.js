      var markersArray = new Array();
      var dbinfo = new Array();
      var droneID = "";
      var map;

      function initialize() {
        var mapOptions = {
          zoom: 8,
          mapTypeId: google.maps.MapTypeId.ROADMAP
        };
        map = new google.maps.Map(document.getElementById('map_canvas'),mapOptions);

        if(navigator.geolocation) {
          navigator.geolocation.getCurrentPosition(function(position) {
            var pos = new google.maps.LatLng(position.coords.latitude,
                                             position.coords.longitude);


            map.setCenter(pos);
          }, function() {
            handleNoGeolocation(true);
          });
        } else {
          // Browser doesn't support Geolocation
          handleNoGeolocation(false);
        }
        getMarkers();
      }


      function getMarkers() {
          var counter = 0;

              if (markersArray) {
                for (i in markersArray) {
                  markersArray[i].setMap(null);
                }
              }

              $.getJSON('././php/getstatus.php', function(data) {
                   $.each(data, function(key, val) {   
                         dbinfo.push(new Array(val.droneid,val.city,val.hight,val.speed,val.stcordinationx,val.stcordinationy,val.cucordinationx,val.cucordinationy,val.decordinationx,val.decordinationy,val.distance,val.distancemade,val.dbattery,val.pbattery,val.dronetype,val.dronestatus));
                  });
              });               


             for (var i = 0; i < dbinfo.length; i++) 
             {
                  var image = './images/plane.png';
                  var point = new google.maps.LatLng(parseFloat(dbinfo[i][6]),parseFloat(dbinfo[i][7]));
                  var marker = new google.maps.Marker({
                    map: map,
                    position: point,
                    icon: image
                  });


                  markersArray.push(marker);
                  marker.set('info', dbinfo[i]);                

                  google.maps.event.addListener(marker, 'click', function() {
                    var info = this.get('info');
                    map.setZoom(15);
                    map.setCenter(this.getPosition());
                    droneID = info[0];
                    $('#city').html("City: " + info[1]);
                    $('#hight').html("Hight: " + info[2] + " Meter");
                    $('#speed').html("Speed: " + info[3] + " Kph");
                    $('#stcordination').html("Start Point: " + info[4] + "," + info[5]);
                    $('#cucordination').html("Current Point: " + info[6] + "," + info[7]);
                    $('#decordination').html("End Point: " + info[8] + "," + info[9]);
                    $('#distance').html("Distance: " + info[10] + " Meter");
                    $('#distancemade').html("Distance from end point: " + info[11] + " Meter");
                    $('#dbattery').html("Drone's Battery: " + info[12] + "%");
                    $('#pbattery').html("Phone's Battery: " + info[13] + "%");
                    $('#dronetype').html("Drone Type: " + info[14]);
                  });
            
                 if(droneID == dbinfo[i][0]){
                    $('#city').html("City: " + dbinfo[i][1]);
                    $('#hight').html("Hight: " + dbinfo[i][2] + " Meter");
                    $('#speed').html("Speed: " + dbinfo[i][3] + " Kph");
                    $('#stcordination').html("Start Point: " +dbinfo[i][4] + "," + dbinfo[i][5]);
                    $('#cucordination').html("Current Point: " + dbinfo[i][6] + "," + dbinfo[i][7]);
                    $('#decordination').html("End Point: " + dbinfo[i][8] + "," + dbinfo[i][9]);
                    $('#distance').html("Distance: " + dbinfo[i][10] + " Meter");
                    $('#distancemade').html("Distance from end point: " + dbinfo[i][11] + " Meter");
                    $('#dbattery').html("Drone's Battery: " + dbinfo[i][12] + "%");
                    $('#pbattery').html("Phone's Battery: " + dbinfo[i][13] + "%");
                    $('#dronetype').html("Drone Type: " + dbinfo[i][14]);
                 }
               }

               // for (var i = 0; i < dbinfo.length; i++) {
               //   if(droneID == dbinfo[i][0]) counter++;
               // }

               // if(counter == 0){
               //      $('#city').html("City: ");
               //      $('#hight').html("Hight: ");
               //      $('#speed').html("Speed: ");
               //      $('#stcordination').html("Start Point: ");
               //      $('#cucordination').html("Current Point: ");
               //      $('#decordination').html("End Piont: ");
               //      $('#distance').html("Distance: ");
               //      $('#distancemade').html("Distance Made: ");
               //      $('#dbattery').html("Drone Battery: ");
               //      $('#pbattery').html("Phone Battery: ");
               //      $('#dronetype').html("Drone Type: ");                
               // }

               for (var i = 0; i < dbinfo.length; i++) {
                    dbinfo.pop();
               }
               dbinfo.length = 0;

              setTimeout("getMarkers()",1000);
      }     



      function handleNoGeolocation(errorFlag) {
        if (errorFlag) {
          var content = 'Error: The Geolocation service failed.';
        } else {
          var content = 'Error: Your browser doesn\'t support geolocation.';
        }

        var options = {
          map: map,
          position: new google.maps.LatLng(60, 105),
          content: content
        };

        var infowindow = new google.maps.InfoWindow(options);
        map.setCenter(options.position);
      }

      google.maps.event.addDomListener(window, 'load', initialize);





