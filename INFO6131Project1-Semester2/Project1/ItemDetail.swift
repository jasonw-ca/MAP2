//
//  ItemDetail.swift
//  iDine
//
//  Created by Jason Wang on 2022-10-11.
//

import SwiftUI

struct ItemDetail: View {
    var order1: Order
    
    
    let item: MenuItem
    var body: some View {
        VStack {
            ZStack (alignment: .bottomTrailing){
                Image(item.mainImage)
                    .resizable()
                    //.scaledToFit()
                    .aspectRatio(contentMode: .fit)
                Text("Photo: \(item.photoCredit)")
                    .padding(4)
                    .background(.black)
                    .font(.caption)
                    .foregroundColor(.white)
                    .offset(x:-5, y:-5)
            }
            Text(item.description)
                .padding()
            Button("Order This", action: {
                order1.add(item: item)
            })
            .font(.largeTitle)
            
            .padding()
            .background(.green)
            Spacer()
        }
        .navigationTitle(item.name)
        .navigationBarTitleDisplayMode(.inline)
        
        
    }
}

struct ItemDetail_Previews: PreviewProvider {
    static var previews: some View {
        //NavigationView {
            ItemDetail(order1: Order(), item: MenuItem.example)
        //}
    }
}

