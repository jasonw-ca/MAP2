//
//  CheckoutView.swift
//  iDine
//
//  Created by Jason Wang on 2022-10-13.
//

import SwiftUI

struct CheckoutView: View {
    @ObservedObject var order: Order
    let paymentTypes = ["Cash", "Credit Card", "iDine Points"]
    @State private var paymentType = "Cash"
    @State private var addLoyaltyDetails = false
    @State private var loyaltyNumber = ""
    let tipAmounts = [10, 15, 20, 25, 0]
    @State var tipAmount = 15
    var totalPrice: String {
        let formatter = NumberFormatter()
        formatter.numberStyle = .currency
        
        let total = Double(order.total)
        let tipValue = total / 100 * Double(tipAmount)
        
        return formatter.string(from: NSNumber(value: total + tipValue)) ?? "$0"
    }
    @State private var showingPaymentAlert = false
    var body: some View {
        NavigationView{
            Form{
                Section{
                    Picker("How do you want to pay?", selection: $paymentType){
                        ForEach(paymentTypes, id: \.self) {
                                Text($0)
                        }
                    }
                    Toggle("Are you a member?", isOn: $addLoyaltyDetails.animation())
                    if addLoyaltyDetails {
                        TextField("Enter you number", text: $loyaltyNumber)
                    }
                }
                Section(header: Text("How much tip?")) {
                    Picker("Percentage", selection: $tipAmount, content: {
                        ForEach(tipAmounts, id: \.self){
                            Text("\($0)%")
                        }
                    })
                    .pickerStyle(.segmented)
                }
                Section(header: Text("Total: \(totalPrice)")
                    .font(.largeTitle)
                    ){
                        Button("Confirm the order"){
                            showingPaymentAlert.toggle()
                        }
                    }
            }
            .navigationTitle("Payment")
            .navigationBarTitleDisplayMode(.inline)
            .alert(isPresented: $showingPaymentAlert){
                Alert(title: Text("Order confirmed"), message: Text("Your total is \(totalPrice) - thank you!"), dismissButton: .default(Text("OK")))
            }
        }
        
        
    }
}

struct CheckoutView_Previews: PreviewProvider {
    static var previews: some View {
        CheckoutView(order: Order())
    }
}
