//
//  ContentView.swift
//  iDine
//
//  Created by Jason Wang on 2022-10-02.
//

import SwiftUI

struct ContentView: View {
    //@State var fullscreen = false
    //@State var buttonText = true
    var order: Order
    let menu = Bundle.main.decode([MenuSection].self, from: "menu.json")
    var body: some View {
        NavigationView {
            List {
                ForEach(menu) { section in
                    Section (header: Text(section.name)) {
                        ForEach(section.items) { item in
                            NavigationLink(destination: ItemDetail(order1: order, item: item)){
                                ItemRow(item: item)
                            }
                            
                        }
                    }
                }
               
                    
            }
            .navigationTitle("Menu")
            //.navigationBarHidden(fullscreen)
//            .navigationBarItems(trailing: Button(buttonText ? "Full" : "Part"){
//                fullscreen.toggle()
//                buttonText.toggle()
//            })
            .listStyle(GroupedListStyle())
            
            
        }
        //.statusBarHidden(fullscreen)
        
           
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView(order: Order())
    }
}
