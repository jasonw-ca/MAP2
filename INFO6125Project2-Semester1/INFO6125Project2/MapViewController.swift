//
//  MapViewController.swift
//  INFO6125Project2
//
//  Created by Sampath Bandara on 2022-08-08.
//

import UIKit
import MapKit
import CoreData

class MapViewController: UIViewController {

    //@IBOutlet weak var mapView: MKMapView!
    @IBOutlet weak var mapView: MKMapView!
    
    private var records: [MoneyRecord] = []
    var coordinates: [CLLocation] = []
    
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var mainTitle: String = ""
    
    //var locationsArray: [[Double]] = [[42.9865996,-81.2780933, 400],[42.9795996,-81.2800933, 300],[42.9685996,-81.2790933, 250],[42.9575996,-81.2780933, 134],[42.9465996,-81.2770933, 245]]
    
    // This needs to show the current location on the map
   //private let locationManager = CLLocationManager()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        // This is needed when showing the current location on the map
        //locationManager.requestWhenInUseAuthorization()
        
        // To get the location info saved in the database
        loadFromDB()
        
        //addNewAnnotation(location: locationsArray)
        setupMap()
        //addAnnotation(location: CLLocation(latitude: latitude, longitude: longitude))
        
        for coord in coordinates {
            addAnnotation(location: coord)
        }
        
        
    }
    
    @IBAction func goToHome(_ sender: UIButton) {
        dismiss(animated: true)
    }
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */
    
    private func loadFromDB (){
        guard let context = getCoreContext() else{
            return
        }
        let request = MoneyRecord.fetchRequest()
        do{
            try records = context.fetch((request))
            
            //print("A:\(records.last!.lat)")
            if(records.count != 0){
                latitude = records.last!.lat
                longitude = records.last!.lng
                
                for record in records {
                    let clLocation = CLLocation(latitude: record.lat, longitude: record.lng)
                    coordinates.append(clLocation)
                }

            }
            
        }catch{
            print(error)
        }
        
    }
    
    private func getCoreContext()->NSManagedObjectContext?{
        (UIApplication.shared.delegate as? AppDelegate)?.persistentContainer.viewContext
    }

    private func setupMap() {
        
        // Set Delegate
        mapView.delegate = self
        
        // Enable showing user location on map (required when showing the user current location)
        //mapView.showsUserLocation = true
        
        // 42.9965996,-81.2780933
        //latitude = 42.9965996
        //longitude = -81.2780933
        
        let location = CLLocation(latitude: latitude, longitude: longitude)
        let radiusInMeters: CLLocationDistance = 100000
        
        let region = MKCoordinateRegion(center: location.coordinate, latitudinalMeters: radiusInMeters, longitudinalMeters: radiusInMeters)
        
        mapView.setRegion(region, animated: true)
        
        // Camera boundaries
        let cameraBoundary = MKMapView.CameraBoundary(coordinateRegion: region)
        mapView.setCameraBoundary(cameraBoundary, animated: true)
        
        // Control zooming
        let zoomRange = MKMapView.CameraZoomRange(maxCenterCoordinateDistance: 100000)
        mapView.setCameraZoomRange(zoomRange, animated: true)
    }
    

    private func addAnnotation(location: CLLocation) {
       
        let annotation = MyAnnotation(coordinate: location.coordinate, title: mainTitle,
                                      subtitle: "",
                                      glyphText: "$")
        mapView.addAnnotation(annotation)
        

    }
    /**
    private func addNewAnnotation(location: [[Double]]) {

       
        for location in locationsArray {
            
            latitude = location[0]
            longitude = location[1]
            let title = location[2]
            
            //print("Location from array: \(locationsArray)")
            print("(\(latitude),\(longitude)")
            
            let newLocation: CLLocation
            
            newLocation = CLLocation(latitude: latitude, longitude: longitude)
            
            let annotation = MyAnnotation(coordinate: newLocation.coordinate, title: "$\(title)", subtitle: "", glyphText: "$")
            
            mapView.addAnnotation(annotation)
        }
        
        // Set Delegate
        mapView.delegate = self
        
        
        let location = CLLocation(latitude: latitude, longitude: longitude)
        let radiusInMeters: CLLocationDistance = 5000
        
        let region = MKCoordinateRegion(center: location.coordinate, latitudinalMeters: radiusInMeters, longitudinalMeters: radiusInMeters)
        
        mapView.setRegion(region, animated: true)
        
        // Camera boundaries
        let cameraBoundary = MKMapView.CameraBoundary(coordinateRegion: region)
        mapView.setCameraBoundary(cameraBoundary, animated: true)
        
        // Control zooming
        let zoomRange = MKMapView.CameraZoomRange(maxCenterCoordinateDistance: 100000)
        mapView.setCameraZoomRange(zoomRange, animated: true)

    }
     
     private func getMyLocation() -> CLLocation {
         return CLLocation(latitude: 42.9965996, longitude: -81.2780933)
     }
     
     */
}

extension MapViewController: MKMapViewDelegate {
    func mapView(_ mapView: MKMapView, viewFor annotation: MKAnnotation) -> MKAnnotationView? {
        let identifier = "myIdentifier"
        var view: MKMarkerAnnotationView
        
        // Check to see if we have a view we can reuse
        if let dequeuedView = mapView.dequeueReusableAnnotationView(withIdentifier: identifier) as? MKMarkerAnnotationView{
            // Get updated annotation
            dequeuedView.annotation = annotation
            // Use our reusable view
            view = dequeuedView
        } else {
            view = MKMarkerAnnotationView(annotation: annotation, reuseIdentifier: identifier)
            view.canShowCallout = true
            
            // Set the position of the callout
            view.calloutOffset = CGPoint(x: 0, y: 10)
            
            // Add a button to right side of callout
            let button = UIButton(type: .detailDisclosure)
            button.tag = 100
            view.rightCalloutAccessoryView = button
            
            // Add an image to left side of callout
            let image = UIImage(systemName: "banknote")
            view.leftCalloutAccessoryView = UIImageView(image: image)
            
            // Change color of pin/marker
            view.markerTintColor = UIColor.systemCyan
            
            // Change color of accessories
            view.tintColor = UIColor.systemCyan
            
            if let myAnnotation = annotation as? MyAnnotation {
                view.glyphText = myAnnotation.glyphText
            }
        }
        
        return view
    }
    
    func mapView(_ mapView: MKMapView, annotationView view: MKAnnotationView, calloutAccessoryControlTapped control: UIControl) {
        print("Button pressed!\(control.tag)")
        
        guard let coordinates = view.annotation?.coordinate else {
            return
        }
        
        let launchOptions = [
            MKLaunchOptionsDirectionsModeKey: MKLaunchOptionsDirectionsModeDriving
        ]
        
        let mapItem = MKMapItem(placemark: MKPlacemark(coordinate: coordinates))
        mapItem.openInMaps(launchOptions: launchOptions)
    }
}

class MyAnnotation: NSObject, MKAnnotation {
    var coordinate: CLLocationCoordinate2D
    var title: String?
    var subtitle: String?
    var glyphText: String?
    
    init(coordinate: CLLocationCoordinate2D, title: String, subtitle: String, glyphText: String? = nil) {
        self.coordinate = coordinate
        self.title = title
        self.subtitle = subtitle
        self.glyphText = glyphText
        
        super.init()
    }
}
