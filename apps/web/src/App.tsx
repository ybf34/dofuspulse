import './App.css'
import LoadingSpinner from "@/components/loading-spinner.tsx";
import {ModeToggle} from "@/components/mode-toggle.tsx";
import Component from "@/components/gear-set.tsx";

function App() {

  return (
      <div>
        <ModeToggle/>
        <LoadingSpinner/>
        <Component/>
      </div>
  )
}

export default App

