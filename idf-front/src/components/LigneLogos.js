

export function LigneLogo({color,ligne}) {

    return (

        <div style={{height:'30px' ,  width: '30px' , borderRadius: '50%', backgroundColor: `${color}`,borderStyle: 'solid',margin:'4%',marginLeft:'6%' ,display:'inline-block'}} >
            <p style={{color:'white', fontSize:'100%' , margin:'5.5px' , }}>{ligne}</p>
        </div>

    )
}