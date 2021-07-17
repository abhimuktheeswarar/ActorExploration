import SwiftUI
import shared

func greet() -> String {
    return Greeting().greeting()
}

struct ContentView: View {
    
    @ObservedObject var counterStateObservable = CounterStateObservable()
    
    var body: some View {
      
        VStack(alignment: .center)    {
            HStack( content: {
                Spacer()
                Button("DECREMENT")  {counterStateObservable.dispatch(action: CounterActionDecrementAction())}
                Spacer()
                Text("\(counterStateObservable.state.counter)")
                Spacer()
                Button("INCREMENT")  {counterStateObservable.dispatch(action: CounterActionIncrementAction())}
                Spacer()
            })
        }
    }
}
struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}

class CounterStateObservable: ObservableObject {
    
    private lazy var viewModel = CounterViewModel(storeType: StoreType.basicV2)
    
    @Published var  state : CounterState = CounterState(counter: 0)
    
    init() {
        
        viewModel.states.collect(collector: Collector<CounterState> { counterState in
            self.state = counterState
            
        }) { (unit, error) in
            // code which is executed if the Flow object completed
        }
        
    }
    
    func dispatch(action : Action)  {
        viewModel.dispatch(action: action)
    }
}

class Collector<T>: Kotlinx_coroutines_coreFlowCollector {
    
    let callback:(T) -> Void
    
    init(callback: @escaping (T) -> Void) {
        self.callback = callback
    }
    
    
    func emit(value: Any?, completionHandler: @escaping (KotlinUnit?, Error?) -> Void) {
        // do whatever you what with the emitted value
        callback(value as! T)
        
        // after you finished your work you need to call completionHandler to
        // tell that you consumed the value and the next value can be consumed,
        // otherwise you will not receive the next value
        //
        // i think first parameter can be always nil or KotlinUnit()
        // second parameter is for an error which occurred while consuming the value
        // passing an error object will throw a NSGenericException in kotlin code, which can be handled or your app will crash
        completionHandler(KotlinUnit(), nil)
    }
}

