import { View, Text, StyleSheet } from "react-native";


export default function (props) {
  return (
     <View style={styles.container}>
       <View style={styles.monthContainer}>
         <Text style={styles.month}>{props.month}</Text>
      </View>
      <View style={styles.amountContainer}>
         <Text style={styles.amountText}>{props.amount} ML</Text>
       </View>
     </View>
    
  );
}

const styles = StyleSheet.create({
  container: {
    flexDirection: "row",
    marginHorizontal: 10,
    justifyContent:"space-between",
    borderColor:"#4287f5",
    borderWidth:2,
    borderRadius:10,
    marginTop:10,
    
    
  },
  monthContainer: {
    backgroundColor: "#4287f5",
    borderRadius: 12,
    marginRight: 10,
    alignItems: "center",
    justifyContent: "center",
    width: 75,
    height: 50,
    margin:5,
  },
  month: {
    color: "#fff",
    fontSize: 20,
  },
  amountContainer: {
    backgroundColor: "#4287f5",
    borderRadius: 12,

    justifyContent: "center",
    alignItems: "center",
    flex: 1,
    paddingHorizontal: 10,
    margin:5,
    
    
  },
  amountText: {
    color: "#fff",
    
    fontSize: 20,
  },
});
