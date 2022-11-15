import React from "react";
import { View, StyleSheet, Text, Button, Snackbar } from "react-native";
import Weather from "./Weather";
import Main from "./Main";
import { useState } from "react";

export default function Home() {

    // const [isLoading, setLoading] = useState(true);
//  var dataToMain = "notyet";
//     const callBack = (data) => {
    
//     dataToMain = data;
//     console.log("Callback: ", dataToMain);
//     setLoading(false)

//   };
//   if (isLoading) {
//     return <Text>Loading...</Text>
//   }

  return (
    <View style={styles.container}>
      <Weather />
      <Main />
    </View>
  );
}
const styles = StyleSheet.create({
  container: {
    flex: 1,

    flexDirection: "column",
    backgroundColor: "white",
    alignItems: "center",
    justifyContent: "center",
  },
});
