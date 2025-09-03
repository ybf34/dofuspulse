import type React from "react";
import {cn} from "@/lib/utils";

interface CharacterClassAvatarProps {
  imageSrc: string;
  size?: number;
  altText: string;
  isSelected: boolean;
}

const CharacterClassAvatar: React.FC<CharacterClassAvatarProps> = ({
                                                                     imageSrc,
                                                                     size = 48,
                                                                     altText,
                                                                     isSelected,
                                                                   }) => {
  const s = Math.round(size);

  return (
      <div
          style={{width: s, height: s}}
          className={cn(
              "relative overflow-hidden rounded-xl",
              isSelected
                  ? "opacity-100 saturate-125 contrast-110 brightness-110"
                  : "opacity-70 grayscale",
          )}
      >
        <img
            src={imageSrc}
            width={s}
            height={s}
            alt={altText}
            draggable={false}
            style={{
              width: "100%",
              height: "100%",
              display: "block",
              objectFit: "cover",
              borderRadius: 12,
              imageRendering: "auto",
              transition: "opacity 200ms ease",
            }}
            decoding="async"
            loading="lazy"
        />
      </div>
  );
};

export default CharacterClassAvatar;
