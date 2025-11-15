import {ReactNode, useEffect, useRef, useState} from 'react';

/** Props for the OverflowSwitch */
type OverflowSwitchProps = {
    children: ReactNode;
    fallback: ReactNode;
}

/** If the child is overflowing the fallback is shown. */
export function OverflowSwitch({
    children,
    fallback
}: OverflowSwitchProps) {
    const ref = useRef<HTMLDivElement>(null);
    const [isOverflowing, setIsOverflowing] = useState(false);

    useEffect(() => {
        const el = ref.current;
        if (!el) {
            return;
        }

        const handleOverflow = () => {
            setIsOverflowing(el.scrollHeight > el.clientHeight || el.scrollWidth > el.clientWidth);
        };

        handleOverflow();

        window.addEventListener('resize', handleOverflow);
        return () => window.removeEventListener('resize', handleOverflow);
    }, [children]);

    return <div
        ref={ref}
        style={{
            overflow: 'hidden',
            whiteSpace: 'nowrap',
            textOverflow: 'ellipsis',
            width: '100%',
            height: '100%',
        }}
    >
        {isOverflowing ? fallback : children}
    </div>;
}