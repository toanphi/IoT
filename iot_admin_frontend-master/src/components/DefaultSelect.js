import { Select } from 'antd';
import React from 'react';


class DefaultSelect extends React.Component {

    constructor(props){
        super(props);

        this.state = {

        }
    }

    handleChange = (value) => {
        console.log(`selected ${value}`);
    }

    render() {
        return <>
            <Select defaultValue="wireless" style={{ width: 120 }} onChange={this.handleChange}>
                {this.props.optionsLst}
            </Select>
        </>
    }

}

export default DefaultSelect;