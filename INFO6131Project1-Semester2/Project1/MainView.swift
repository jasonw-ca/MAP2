//
//  MainView.swift
//  iDine
//
//  Created by Jason Wang on 2022-10-12.
//

import SwiftUI

struct MainView: View {
    @StateObject var order = Order()
    var body: some View {
        TabView{
            ContentView(order: order)
                .tabItem {
                    Label("Menu", systemImage: "list.dash")
                }
            OrderView(order: order)
                .tabItem({
                    Label("Order", systemImage: "square.and.pencil")
                })
        }
    }
}

struct MainView_Previews: PreviewProvider {
    static var previews: some View {
        MainView(order: Order())
    }
}
