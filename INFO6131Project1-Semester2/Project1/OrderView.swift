//
//  OrderView.swift
//  iDine
//
//  Created by Jason Wang on 2022-10-12.
//

import SwiftUI

struct OrderView: View {
    @ObservedObject var order: Order
    
    func deleteItem(at offsets: IndexSet) {
        order.items.remove(atOffsets: offsets)
    }
    
    var body: some View {
        NavigationView{
            List{
                Section{
                    ForEach(order.items) {item in
                        HStack{
                            Text(item.name)
                            Spacer()
                            Text("$\(item.price)")
                        }
                    }
                    .onDelete(perform: deleteItem)
                }
                Section{
                    NavigationLink(destination: CheckoutView(order: order)){
                        
                            Text("Place Order")
                            
                        
                    }
                    .disabled(order.items.isEmpty)
                }
            }
            .navigationTitle("Order")
            .listStyle(.insetGrouped)
            .toolbar{
                EditButton()
            }
        }
    }
}

struct OrderView_Previews: PreviewProvider {
    static var previews: some View {
        OrderView(order: Order())
    }
}
