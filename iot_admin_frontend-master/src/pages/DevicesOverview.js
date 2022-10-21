import React from 'react';
import { Layout, Statistic, Row, Col } from 'antd';

import AntdSelect from '../components/AntdSelect';
import DATable from '../components/DATable';
import { Button } from 'antd';
import '../App.css'

class DevicesOverview extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            isAddForm: false,
            isAddFormAnimation: true,
            recordNums: "0",
            pageSize: "5",
            sort: []
        }
    }
    // AntdSelect data
    numberOfRows = ['5', '10', '15']

    render() {
        if (this.props.isDevicesOverview !== '1') {
            return ""
        }
        return <Layout style={{ background: 'white', minHeight: '70vh', zIndex: 1, transition: 'all 1s' }} id={this.state.isAddForm ? "device-overview-after" : ""}>
                <div style={{
                    padding: '2px',
                    display: 'flex',
                    width: '100%'
                }}>
                    <Row gutter={16} style={{ background: 'white', margin: '0', padding: '0', width: '80%' }}>
                        <Col span={4}>
                            <Statistic title="Tổng số:" value={this.state.recordNums} suffix="" />
                        </Col>
                        <Col span={6}>
                            <label style={{ color: '#777', width: '100%', fontSize: '14px' }}>Số lương bản ghi:</label>
                            <AntdSelect
                                placeholder="Displayed rows"
                                optionsLst={this.numberOfRows}
                                defaultValue={this.state.pageSize}
                                onValueChange={this.onTablePagination}
                                style={{ width: '150px' }}
                            ></AntdSelect>
                        </Col>
                    </Row>
                    <div style={{ width: '20%', justifyContent: 'center', display: 'flex' }}>
                        <Button
                            type="primary"
                            size="default"
                            shape="circle"
                            style={{ float: 'right', margin: 'auto', marginLeft: '70%', minWidth: '4em', minHeight: '4em' }}
                            onClick={this.addBtnOnClick}
                        >
                            Thêm
                        </Button>
                    </div>


                </div>

                <div style={{ marginTop: '20px' }}>
                    <label style={{ marginTop: '20px' }}>Thống kê thiết bị:</label>
                    <DATable
                        addBtnOnClick={this.addBtnOnClick}
                        pageSize={this.state.pageSize}
                        isAddForm={this.state.isAddForm}
                        isAddFormAnimation={this.state.isAddFormAnimation}
                        onRecordNumsChange={this.onRecordNumsChange}
                    ></DATable>
                </div>
            </Layout>
    }

    // functions

    onRecordNumsChange = (num) => {
        this.setState({
            recordNums: num
        })
    }
    addBtnOnClick = (event) => {
        try {
            if (event.target.innerHTML === "Cancel") {
                event.preventDefault()
            }
        } catch (e) {

        }

        // turn animation
        this.setState({
            isAddForm: !this.state.isAddForm,
        })

        // if animation is off, wait and remove element else render 
        if (this.state.isAddFormAnimation === false) {
            setTimeout(() => {
                this.setState({
                    isAddFormAnimation: !this.state.isAddFormAnimation
                })
            }, 1100)
        } else {
            this.setState({
                isAddFormAnimation: !this.state.isAddFormAnimation,
            })
        }
    }

    onTablePagination = (nor) => {
        this.setState({
            pageSize: nor
        })
    }

}

export default DevicesOverview;