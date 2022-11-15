import React from 'react';
import {View, StyleSheet} from "react-native";
import Profile from './Profile';
import Notification from './Notification';

export default function Settings () {
    return(
        <View style={styles.container}>
            <Profile/>
            <Notification/>
        </View>
    )
}

const styles = StyleSheet.create({
    container: {
      flex: 1,
      flexDirection: "column",
      
      alignItems: "center",
      justifyContent: "center",
    },
  });