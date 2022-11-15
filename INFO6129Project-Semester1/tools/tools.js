import * as Location from "expo-location";

export function today() {
  const date = new Date();

  let day = date.getDate();
  let month = date.getMonth() + 1;
  let year = date.getFullYear();

  let currentDate = `${day}-${month}-${year}`;
  console.log(currentDate);
  return currentDate;
}

export function thisMonth() {
  const date = new Date();

  return date.getMonth() + 1;
}

export async function getLocation() {
  
  try {
    const enabled = await Location.hasServicesEnabledAsync();
    if (!enabled) {
      return;
    }
    const getResponse = await Location.getForegroundPermissionsAsync();
    //console.log("GetResponse: ", getResponse);

    let granted = getResponse.granted;
    if (!granted) {
      const requestResponse =
        await Location.requestForegroundPermissionsAsync();
      //console.log("RequestResponse: ", requestResponse);
      granted = requestResponse.granted;
    }
    if (!granted) {
      //console.log("Location not authorizaed!");
      return;
    }

    const location = await Location.getCurrentPositionAsync({
      accuracy: Location.Accuracy.Lowest,
    });

    const latitude = location.coords.latitude;
    const longitude = location.coords.longitude;
    
    return { latitude, longitude };
  } catch (e) {
    console.log("Error: ", e);
  }
  
}
