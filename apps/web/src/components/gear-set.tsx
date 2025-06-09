import {Badge} from "@/components/ui/badge"
import {Copy} from "lucide-react"

const builds = [
  {
    title: "Test gearset",
    tags: ["label", "label", "label", "label"],
  }
]

const EquipmentSlot = ({className = ""}: { className?: string }) => (
    <button
        className={`col-start-1 row-start-1 flex size-10 bg-secondary p-1 ${className}`}
        style={{
          backgroundImage: `url('/images/item.png')`,
          backgroundSize: '80%',
          backgroundPosition: 'center',
          backgroundRepeat: 'no-repeat',
        }}
    >
    </button>
)


const PlayerImage = () => (
    <img
        src="/images/player.png"
        alt="Player Character"
        className="col-span-2 col-start-3 row-span-3 h-[124px] w-[84px] shrink-0 object-contain" // object-contain to prevent cropping
    />
)

export default function GearSet() {
  return (
      <div className="min-h-screen bg-background p-4">
        <div
            className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4 max-w-7xl mx-auto">
          {builds.map((build, index) => (
              <a key={index}
                 className="flex flex-col gap-3 bg-background p-3 ring-1 ring-inset ring-border">
                <header className="w-[250px]">
                  <div className="flex w-full items-center justify-between gap-1">
                    <button className="truncate text-start">{build.title}</button>
                    <button className="group rounded-full p-1.5 hover:bg-secondary">
                      <Copy
                          className="size-4 stroke-muted-foreground transition-colors group-hover:stroke-foreground"/>
                    </button>
                  </div>
                  <ul className="mt-2 flex h-12 flex-wrap gap-1">
                    {build.tags.map((tag, tagIndex) => (
                        <li key={tagIndex}>
                          <Badge
                              variant="secondary"
                              className="h-fit rounded-full border-transparent bg-secondary text-secondary-foreground hover:bg-secondary/80"
                          >
                            {tag}
                          </Badge>
                        </li>
                    ))}
                  </ul>
                </header>

                <div className="shrink-0 bg-border h-[1px] w-full"/>

                <div className="grid grid-cols-6 grid-rows-5 gap-0.5">

                  <EquipmentSlot className="col-start-1 row-start-1"/>
                  <EquipmentSlot className="col-start-1 row-start-2"/>
                  <EquipmentSlot className="col-start-1 row-start-3"/>
                  <EquipmentSlot className="col-start-1 row-start-4"/>

                  <EquipmentSlot className="col-start-6 row-start-1"/>
                  <EquipmentSlot className="col-start-6 row-start-2"/>
                  <EquipmentSlot className="col-start-6 row-start-3"/>
                  <EquipmentSlot className="col-start-6 row-start-4"/>

                  <EquipmentSlot className="col-start-1 row-start-5"/>
                  <EquipmentSlot className="col-start-2 row-start-5"/>
                  <EquipmentSlot className="col-start-3 row-start-5"/>
                  <EquipmentSlot className="col-start-4 row-start-5"/>
                  <EquipmentSlot className="col-start-5 row-start-5"/>
                  <EquipmentSlot className="col-start-6 row-start-5"/>

                  <EquipmentSlot className="col-start-3 row-start-4"/>
                  <EquipmentSlot className="col-start-4 row-start-4"/>

                  <PlayerImage/>
                </div>
              </a>
          ))}
        </div>
      </div>
  )
}
