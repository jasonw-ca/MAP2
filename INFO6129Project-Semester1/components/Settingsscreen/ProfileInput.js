import React, { useState } from "react";
import {
  KeyboardAvoidingView,
  View,
  TextInput,
  Button,
  StyleSheet,
} from "react-native";

// import styles from "./ProfileInputStyle";

export default function (props) {
  const [name, setName] = useState("");
  const [gender, setGender] = useState("");
  const [weight, setWeight] = useState("");
  const [height, setHeight] = useState("");
  const handleAddProfile = (name, gender, height, weight) => {
    props.addProfile(name, gender, height, weight);
  // const dismiss = () => {
    
  // };

    setName(null);
    setGender(null);
    setWeight(null);
    setHeight(null);
  };

  return (
    <KeyboardAvoidingView
      behavior={Platform.OS === "ios" ? "padding" : "height"}
      style={styles.viewWrapper}
    >
      <View style={styles.modalView}>
        <TextInput
          placeholder="Enter name"
          value={name}
          style={styles.textInput}
          onChangeText={(value) => setName(value)}
        />
        <TextInput
          placeholder="M/F"
          value={gender}
          style={styles.textInput}
          onChangeText={(value) => setGender(value)}
        />
        <TextInput
          placeholder="KG"
          value={weight}
          style={styles.textInput}
          onChangeText={(value) => setWeight(value)}
        />
        <TextInput
          placeholder="cm"
          value={height}
          style={styles.textInput}
          onChangeText={(value) => setHeight(value)}
        />

        <View style={{flexDirection:"row", justifyContent:"space-around", width:"100%"}}>

          <Button title="Cancel" onPress={props.dismiss} />
          <Button
            title="Confirm"
            onPress={() => {
              handleAddProfile(name, gender, height, weight);
            }}
          />
          
        </View>
      </View>
    </KeyboardAvoidingView>
  );
}

const styles = StyleSheet.create({
  container: {
    flexDirection: "column",
    flex: 1,

    justifyContent: "center",
    borderColor: "#4287f5",
    borderWidth: 2,
    borderRadius: 10,
    marginTop: 10,
    width: "95%",
    paddingBottom: 10,
  },
  heading: {
    backgroundColor: "#4287f5",

    alignItems: "center",
    justifyContent: "center",
  },
  headingText: {
    color: "#fff",
    fontSize: 20,
  },
  subContainer: {
    flexDirection: "row",

    justifyContent: "space-around",
    alignItems: "center",
    flex: 1,
    // paddingHorizontal: 10,
    // margin: 5,
  },
  text: {
    color: "#000",
    fontSize: 20,
    textAlign: "center",
  },
  viewWrapper: {
    flex: 1,
    alignItems: "center",
    justifyContent: "center",
    backgroundColor: "rgba(0, 0, 0, 0.2)",
  },
  modalView: {
    alignItems: "center",
    justifyContent: "center",
    position: "absolute",
    top: "20%",
    left: "10%",
    elevation: 5,
    height: 380,
    width: "80%",
    backgroundColor: "#fff",
    borderRadius: 7,
  },
  textInput: {
    width: "80%",
    borderRadius: 5,
    paddingVertical: 8,
    paddingHorizontal: 16,
    borderColor: "rgba(0, 0, 0, 0.2)",
    borderWidth: 1,
    marginBottom: 8,
  },
});
